package io.github.bumblesoftware.fastload.mixin.mixins.mc1182.client;

import io.github.bumblesoftware.fastload.client.FLClientEvents.RecordTypes.PauseMenuEventContext;
import io.github.bumblesoftware.fastload.client.FLClientEvents.RecordTypes.SetScreenEventContext;
import io.github.bumblesoftware.fastload.client.FLClientEvents.RecordTypes.TickEventContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.*;

@Mixin(MinecraftClient.class)
public class MinecraftClientEvents {
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
}
