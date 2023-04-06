package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.client.FLClientEvents.RecordTypes.PauseMenuEventContext;
import io.github.bumblesoftware.fastload.client.FLClientEvents.RecordTypes.SetScreenEventContext;
import io.github.bumblesoftware.fastload.client.FLClientEvents.RecordTypes.TickEventContext;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.integrated.IntegratedServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.*;
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreenEvent(final Screen screen, final CallbackInfo ci) {
        SET_SCREEN_EVENT.fireEvent(new SetScreenEventContext(screen, ci));
    }

    @Inject(method = "openPauseMenu", at = @At("HEAD"), cancellable = true)
    private void openPauseMenuEvent(boolean pause, CallbackInfo ci) {
        PAUSE_MENU_EVENT.fireEvent(new PauseMenuEventContext(pause, ci));

    }

    @Inject(method = "render", at = @At("HEAD"))
    private void renderEvent(boolean tick, CallbackInfo ci) {
        RENDER_TICK_EVENT.fireEvent(new TickEventContext(tick));
    }

    @Redirect(method = "startIntegratedServer(Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Function;" +
            "ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V", at = @At(value = "INVOKE", target = "Lnet" +
            "/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 2))
    private void remove441(MinecraftClient client, @Nullable Screen screen) {
        if (FLMath.isPreRenderEnabled())
            client.setScreen(ABSTRACTED_CLIENT.newBuildingTerrainScreen());
    }

    @Redirect(method = "joinWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void removeProgressScreen(MinecraftClient client, Screen screen) {
    }

    @Redirect(method = "startIntegratedServer(Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Function;" +
            "ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/integrated/IntegratedServer;isLoading()Z"))
    private boolean removeWait(IntegratedServer integratedServer) {
        return true;
    }
}
