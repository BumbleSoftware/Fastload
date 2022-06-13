package io.github.bumblesoftware.fastload.mixin;

import net.minecraft.client.MinecraftClient;

import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.SaveLoader;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    //Original code is from 'kennytv, forceloadingscreen'. Code is modified to our needs.
    @Shadow
    public void setScreen(@Nullable Screen screen) {}
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreen(final Screen screen, final CallbackInfo ci) {
        if (screen instanceof DownloadingTerrainScreen) {
            render(true);
            ci.cancel();
            setScreen(null);
            justLoaded = true;
        }
    }
    @Shadow
    private void render(boolean tick) {}
    @Inject(method = "startIntegratedServer", at = @At("HEAD"))
    private void renderOnStartServer() {
        render(true);
    }
    @Shadow
    public boolean skipGameRender;
    @Shadow private boolean windowFocused;

    @Inject(method = "render", at = @At("HEAD"))
    private void setSkipGameRender(boolean tick, CallbackInfo ci) {
        skipGameRender = false;
    }
    private boolean justLoaded;
    @Shadow private volatile boolean running;
    @Inject(method = "openPauseMenu", at = @At("HEAD"), cancellable = true)
    private void cancelOpenPauseMenu(boolean pause, CallbackInfo ci) {
        if (windowFocused && justLoaded) {
            justLoaded = false;
        }
        else if (!windowFocused && running && justLoaded) {
            ci.cancel();
            justLoaded = false;
        }
    }
}
