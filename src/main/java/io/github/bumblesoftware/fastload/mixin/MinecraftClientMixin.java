package io.github.bumblesoftware.fastload.mixin;

import io.github.bumblesoftware.fastload.MinecraftClientMixinInterface;
import io.github.bumblesoftware.fastload.config.FLConfig;
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
public class MinecraftClientMixin implements MinecraftClientMixinInterface {
    //Original code is from 'kennytv, forceloadingscreen'. Code is modified to our needs.
    @Shadow public void setScreen(@Nullable Screen screen) {}
    @Shadow private boolean windowFocused;
    @Shadow private volatile boolean running;
    private boolean justLoaded = false;
    private boolean shouldLoad = false;
    private boolean playerJoined = false;
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreen(final Screen screen, final CallbackInfo ci) {
        if (screen instanceof DownloadingTerrainScreen && shouldLoad && playerJoined && running && FLConfig.CLOSE_LOADING_SCREEN_UNSAFELY) {
            ci.cancel();
            setScreen(null);
            justLoaded = true;
            shouldLoad = false;
            playerJoined  = false;
        }
    }
    @Inject(method = "openPauseMenu", at = @At("HEAD"), cancellable = true)
    private void cancelOpenPauseMenu(boolean pause, CallbackInfo ci) {
        if(justLoaded) {
            if (windowFocused) {
                justLoaded = false;
            }
            else if (running) {
                ci.cancel();
                justLoaded = false;
            }
        }
    }
    @Override
    public void canPlayerLoad() {
        shouldLoad = true;
    }
    @Override
    public void gameJoined() {
        playerJoined = true;
    }
}
