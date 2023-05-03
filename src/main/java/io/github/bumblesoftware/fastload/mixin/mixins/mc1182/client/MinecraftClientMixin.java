package io.github.bumblesoftware.fastload.mixin.mixins.mc1182.client;

import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.SetScreenEventContext;
import io.github.bumblesoftware.fastload.common.FLCommonEvents.Contexts.ServerContext;
import io.github.bumblesoftware.fastload.util.MutableObjectHolder;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.integrated.IntegratedServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.SET_SCREEN_EVENT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.*;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.BOOLEAN_EVENT;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.SERVER_EVENT;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Locations.SERVER_PSR_LOADING_REDIRECT;

@Restriction(require = @Condition(value = "minecraft", versionPredicates = "1.18.2"))
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow public abstract void setScreen(@Nullable Screen screen);

    @Shadow protected abstract void reset(Screen screen);

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreenEvent(final Screen screen, final CallbackInfo ci) {
        if (SET_SCREEN_EVENT.isNotEmpty())
            SET_SCREEN_EVENT.fire(new SetScreenEventContext(screen, ci));
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void renderEvent(boolean tick, CallbackInfo ci) {
        if (BOOLEAN_EVENT.isNotEmpty(RENDER_TICK))
            BOOLEAN_EVENT.fire(List.of(RENDER_TICK), new MutableObjectHolder<>(tick));
    }

    @Redirect(method = "startIntegratedServer(Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Function;" +
            "ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V", at = @At(value = "INVOKE", target = "Lnet" +
            "/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 2))
    private void handle441Loading(MinecraftClient client, @Nullable Screen screen) {
        if (SET_SCREEN_EVENT.isNotEmpty(LLS_441_REDIRECT))
            SET_SCREEN_EVENT.fire(
                    List.of(LLS_441_REDIRECT),
                    new SetScreenEventContext(screen, null)
            );
        else this.setScreen(screen);
    }

    @Redirect(method = "startIntegratedServer(Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Function;" +
            "ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/integrated/IntegratedServer;isLoading()Z"))
    private boolean handleServerWait(IntegratedServer integratedServer) {
        final var returnValue = new MutableObjectHolder<>(integratedServer.isLoading());
        if (SERVER_EVENT.isNotEmpty(SERVER_PSR_LOADING_REDIRECT))
            SERVER_EVENT.fire(
                    List.of(SERVER_PSR_LOADING_REDIRECT),
                    new ServerContext<>(integratedServer, returnValue)
            );
        return returnValue.heldObj;
    }

    @Redirect(method = "joinWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void handleProgressScreen(MinecraftClient client, Screen screen) {
        if (SET_SCREEN_EVENT.isNotEmpty(PROGRESS_SCREEN_JOIN_WORLD_REDIRECT))
            SET_SCREEN_EVENT.fire(
                    List.of(PROGRESS_SCREEN_JOIN_WORLD_REDIRECT),
                    new SetScreenEventContext(screen, null)
            );
        else this.reset(screen);
    }
}
