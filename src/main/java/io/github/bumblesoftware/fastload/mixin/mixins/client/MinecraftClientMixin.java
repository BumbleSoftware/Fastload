package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.events.FLEvents;
import io.github.bumblesoftware.fastload.events.FLEvents.RecordTypes.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreenEvent(final Screen screen, final CallbackInfo ci) {
        FLEvents.SET_SCREEN_EVENT.fireEvent(new SetScreenEventContext(screen, ci));
    }
    @Inject(method = "openPauseMenu", at = @At("HEAD"), cancellable = true)
    private void openPauseMenuEvent(boolean pause, CallbackInfo ci) {
        FLEvents.PAUSE_MENU_EVENT.fireEvent(new PauseMenuEventContext(pause, ci));

    }
    @Inject(method = "render", at = @At("HEAD"))
    private void renderEvent(boolean tick, CallbackInfo ci) {
        FLEvents.RENDER_TICK_EVENT.fireEvent(new RenderTickEventContext(tick, ci));
    }
}
