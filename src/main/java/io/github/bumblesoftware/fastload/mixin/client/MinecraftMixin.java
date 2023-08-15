package io.github.bumblesoftware.fastload.mixin.client;

import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.SetScreenEventContext;
import io.github.bumblesoftware.fastload.common.FLCommonEvents.Contexts.ServerContext;
import io.github.bumblesoftware.fastload.util.obj_holders.MutableObjectHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.server.IntegratedServer;
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

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow public abstract void setScreen(@Nullable Screen screen);

    @Shadow protected abstract void updateScreenAndTick(Screen screen);

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreenEvent(final Screen screen, final CallbackInfo ci) {
        if (SET_SCREEN_EVENT.isNotEmpty())
            SET_SCREEN_EVENT.execute(new SetScreenEventContext(screen, ci));
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void renderEvent(boolean tick, CallbackInfo ci) {
        if (BOOLEAN_EVENT.isNotEmpty(RENDER_TICK))
            BOOLEAN_EVENT.execute(List.of(RENDER_TICK), new MutableObjectHolder<>(tick));
    }

    @Redirect(method = "doLoadLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    private void handle441Loading(Minecraft client, @Nullable Screen screen) {
        if (SET_SCREEN_EVENT.isNotEmpty(LLS_441_REDIRECT))
            SET_SCREEN_EVENT.execute(
                    List.of(LLS_441_REDIRECT),
                    new SetScreenEventContext(screen, null)
            );
        else this.setScreen(screen);
    }

    @Redirect(method = "doLoadLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/server/IntegratedServer;isReady()Z"))
    private boolean handleServerWait(IntegratedServer integratedServer) {
        final var returnValue = new MutableObjectHolder<>(integratedServer.isReady());
        if (SERVER_EVENT.isNotEmpty(SERVER_PSR_LOADING_REDIRECT))
            SERVER_EVENT.execute(
                    List.of(SERVER_PSR_LOADING_REDIRECT),
                    new ServerContext<>(integratedServer, returnValue)
            );
        return returnValue.getHeldObj();
    }

    @Redirect(method = "setLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;updateScreenAndTick(Lnet/minecraft/client/gui/screens/Screen;)V"))
    private void handleProgressScreen(Minecraft client, Screen screen) {
        if (SET_SCREEN_EVENT.isNotEmpty(PROGRESS_SCREEN_JOIN_WORLD_REDIRECT))
            SET_SCREEN_EVENT.execute(
                    List.of(PROGRESS_SCREEN_JOIN_WORLD_REDIRECT),
                    new SetScreenEventContext(screen, null)
            );
        else this.updateScreenAndTick(screen);
    }
}
