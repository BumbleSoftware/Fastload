package io.github.bumblesoftware.fastload.mixin;

import net.minecraft.client.MinecraftClient;

import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow public void setScreen(@Nullable Screen screen) {}
    @Inject(at = @At("HEAD"), method = "setScreen", cancellable = true)
    private void setScreen(final Screen screen, final CallbackInfo ci) {
        if (screen instanceof DownloadingTerrainScreen) {
            ci.cancel();
            setScreen(null);
        }
    }
    @Inject(method = "setScreen", at = @At("HEAD"))
    private void injectRenderCall(CallbackInfo ci) {} 
    /* I don't know what to do here?
    You have to somehow call startIntegratedServer() */
}
