package io.github.bumblesoftware.fastload.mixin;

import io.github.bumblesoftware.fastload.util.mixin.MinecraftClientMixinInterface;
import io.github.bumblesoftware.fastload.util.screen.BuildingTerrainScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.bumblesoftware.fastload.config.FLMath.*;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements MinecraftClientMixinInterface {
    //Original code is from 'kennytv, forceloadingscreen' under the 'MIT' License.
    //Code is heavily modified to suit Fastload's needs
    @Shadow public void setScreen(@Nullable Screen screen) {}
    @Shadow private boolean windowFocused;
    @Shadow private volatile boolean running;
    @Shadow @Nullable public ClientWorld world;
    private boolean justLoaded = false;
    private boolean shouldLoad = false;
    private boolean playerJoined = false;
    private boolean isBuilding;
    public void canPlayerLoad() {
        shouldLoad = true;
    }
    @Override
    public void gameJoined() {
        playerJoined = true;
    }
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreen(final Screen screen, final CallbackInfo ci) {
        if (screen instanceof ProgressScreen) {
            ci.cancel();
        }
        if (screen instanceof DownloadingTerrainScreen && shouldLoad && playerJoined && running) {
            shouldLoad = false;
            playerJoined = false;
            justLoaded = true;
            if (getCloseSafe()) {
                ci.cancel();
                setScreen(new BuildingTerrainScreen());
                isBuilding = true;
            } else if (getCloseUnsafe()) {
                ci.cancel();
                setScreen(null);
            }
        }
    }
    @Inject(method = "openPauseMenu", at = @At("HEAD"), cancellable = true)
    private void cancelOpenPauseMenu(boolean pause, CallbackInfo ci) {
        if (justLoaded && !getCloseSafe()) {
            if (windowFocused) justLoaded = false;
            else if (running) {
                justLoaded = false;
                ci.cancel();
            }
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(boolean tick, CallbackInfo ci) {
        if (this.world != null && isBuilding) {
            int chunkLoadedCount = this.world.getChunkManager().getLoadedChunkCount();
            if (chunkLoadedCount >= getPreRenderRadius()) {
                setScreen(null);
                isBuilding = false;
            }
        }
    }
}
