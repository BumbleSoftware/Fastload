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
    //Original code is from 'kennytv, forceloadingscreen'. Code is modified to our needs.
    @Shadow public void setScreen(@Nullable Screen screen) {}
    @Shadow private void render(boolean tick) {}
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreen(final Screen screen, final CallbackInfo ci) {
        if (screen instanceof DownloadingTerrainScreen) {
            render(true);
            ci.cancel();
            setScreen(null);
        }
    }
}
