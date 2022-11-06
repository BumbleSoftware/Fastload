package io.github.bumblesoftware.fastload.mixin;

import io.github.bumblesoftware.fastload.FastLoad;
import io.github.bumblesoftware.fastload.util.mixin.MinecraftClientMixinInterface;
import io.github.bumblesoftware.fastload.util.screen.BuildingTerrainScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.bumblesoftware.fastload.config.FLMath.*;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements MinecraftClientMixinInterface {
    //This constant permits some variables to be printed to diagnose an issue easier.
    private final boolean debug = getDebug();

    //Original code is from 'kennytv, forceloadingscreen' under the 'MIT' License.
    //Code is heavily modified to suit Fastload's needs

    @Shadow public void setScreen(@Nullable Screen screen) {}
    @Shadow private boolean windowFocused;
    @Shadow private volatile boolean running;
    @Shadow @Nullable public ClientWorld world;
    @Shadow @Final public WorldRenderer worldRenderer;
    @Shadow @Final public GameOptions options;

    @Shadow @Final public GameRenderer gameRenderer;
    @Shadow @Nullable public ClientPlayerEntity player;
    //Checkers
    private boolean justLoaded = false;
    private boolean shouldLoad = false;
    private boolean playerJoined = false;
    private boolean showRDDOnce = false;
    //Boolean to Initiate Pre-render
    private boolean isBuilding = false;
    //Pre Renderer Log Constants
    @SuppressWarnings("FieldCanBeLocal")
    private final int chunkTryLimit = getChunkTryLimit();
    //Pitch storage
    private Float oldPitch = null;
    //Warning Constants
    private Integer oldChunkLoadedCountStorage = null;
    private int preparationWarnings = 0;
    private Integer oldChunkBuildCountStorage = null;
    private int buildingWarnings = 0;
    //Ticks until Pause Menu is Active again
    private final int timeDownGoal = 10;
    // Set this to 0 to start timer for Pause Menu Cancellation
    private int timeDown = timeDownGoal;
    //Checks if Player is ready
    public void canPlayerLoad() {
        shouldLoad = true;
    }
    //Checks if Player joined game
    @Override
    public void gameJoined() {
        playerJoined = true;
    }
    private Camera getCamera() {
        return gameRenderer.getCamera();
    }
    //Basic Logger
    private static void log(String toLog) {
        FastLoad.LOGGER.info(toLog);
    }
    //Logs Difference in Render and Pre-render distances
    private static void logRenderDistanceDifference() {
        if (!getPreRenderRadius().equals(getPreRenderRadius(true)))
            log("Pre-rendering radius changed to "
                    + getPreRenderRadius() + " from " + getPreRenderRadius(true)
                    + " to protect from chunks not loading past your given render distance. " +
                    "To resolve this, please adjust your render distance accordingly");
    }
    //Logs Goal Versus amount Pre-renderer could load
    private void logPreRendering(int chunkLoadedCount) {
        log("Goal (Loaded Chunks): " + getPreRenderArea());
        log("Loaded Chunks: " + chunkLoadedCount);
    }
    private void logBuilding(int chunkBuildCount, int chunkBuildCountGoal) {
        log("Goal (Built Chunks): " + chunkBuildCountGoal);
        log("Chunk Build Count: " + chunkBuildCount);
    }
    private void stopBuilding(int chunkLoadedCount, int chunkBuildCount, int chunkBuildCountGoal) {
        if (debug) {
            logBuilding(chunkBuildCount, chunkBuildCountGoal);
            logPreRendering(chunkLoadedCount);
        }
        setScreen(null);
        isBuilding = false;
        if (!windowFocused) {
            timeDown = 0;
            if (debug) log("Temporarily Cancelling Pause Menu to enable Renderer");
        }
        assert this.player != null;
        getCamera().setRotation(this.player.getYaw() , oldPitch);
        this.player.setPitch(oldPitch);
        oldPitch = null;
    }
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreen(final Screen screen, final CallbackInfo ci) {
        //Stop Pause Menu interfering with rendering
        if (timeDown < timeDownGoal && screen instanceof GameMenuScreen && !windowFocused) {
            ci.cancel();
            setScreen(null);
        }
        //Log Pre-Render Initiation
        if (screen instanceof BuildingTerrainScreen) {
            if (debug) log("Successfully Initiated Building Terrain");
        }
        //Close Progress Screen
        if (screen instanceof ProgressScreen && (getCloseUnsafe())) {
            ci.cancel();
            if (debug) log("Progress Screen Successfully Cancelled");
        }
        //Close Downloading Terrain Screen ASAP
        if (screen instanceof DownloadingTerrainScreen && shouldLoad && playerJoined && running) {
            if (debug) log("Downloading Terrain Accessed!");
            shouldLoad = false;
            playerJoined = false;
            justLoaded = true;
            showRDDOnce = true;
            // Switch to Pre-render Phase
            if (getCloseSafe()) {
                ci.cancel();
                if (debug) log("Preparing to replace Download Terrain with Building Terrain");
                if (debug) log("Goal (Loaded Chunks): " + getPreRenderArea());
                justLoaded = true;
                isBuilding = true;
                setScreen(new BuildingTerrainScreen());
            //Skip Downloading Terrain Screen
            } else if (getCloseUnsafe()) {
                ci.cancel();
                if (debug) log("Successfully Skipped Downloading Terrain Screen!");
                timeDown = 0;
                setScreen(null);
            }
        }
    }
    @Inject(method = "openPauseMenu", at = @At("HEAD"), cancellable = true)
    private void cancelOpenPauseMenu(boolean pause, CallbackInfo ci) {
        //Stop Pause for Downloading Terrain Skip (Failsafe)
        if (justLoaded) {
            if (windowFocused) justLoaded = false;
            else if (running) {
                justLoaded = false;
                ci.cancel();
                if (debug) log("Pause Menu Cancelled");
            }
        }
    }
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(boolean tick, CallbackInfo ci) {
        //Log differences differences
        if (showRDDOnce) {
            logRenderDistanceDifference();
            showRDDOnce = false;
        }
        //Pre-rendering Engine
        if (isBuilding) {
            if (this.world != null) {
                //Optimisations
                assert player != null;
                if (oldPitch == null) {
                    oldPitch = this.player.getPitch();
                }
                this.player.setPitch(0);
                if (debug) {
                    log("Pitch:" + player.getPitch());
                }
                int chunkLoadedCount = this.world.getChunkManager().getLoadedChunkCount();
                //This is HIGHLY contingent on where your camera is looking
                int chunkBuildCount = this.worldRenderer.getCompletedChunkCount();
                double FOV = this.options.getFov().getValue();
                double chunkBuildCountGoal = (FOV/360) * getPreRenderArea().doubleValue();
                if (debug) {
                    logPreRendering(chunkLoadedCount);
                    logBuilding(chunkBuildCount, (int) chunkBuildCountGoal);
                }
                final int oldPreparationWarningCache = preparationWarnings;
                final int oldBuildingWarningCache = buildingWarnings;
                //The warning system
                if (oldChunkLoadedCountStorage != null && oldChunkBuildCountStorage != null) {
                    /*
                    The reason for this function (within the if() check, below this comment paragraph)
                    is that wasting time, generating chunks on the server IS NOT pre-rendering!
                    By this time, this module has cancelled your pre-rendering,
                    because it has loaded all your important chunks and ones that were previously generated.
                    This is done by only enabling pre-render cancellations until at least HALF of your goal is loaded.
                    Due to this, it serves no purpose to keep pre-rendering past the given limits.
                    Moreover, the purpose of the GOAL, in the first place, is to simply set a soft cap on how many chunks
                    the renderer is permitted to build, so you can enter your world within a time that you desire!
                    ... Unless you enjoy pre-rendering 3000 chunks (on a 32-Ren-Dist) to enter your game for a 5-minute session. -_-
                    Because, if that's the case, what the hell are you using this mod for????
                    */
                    if (oldChunkLoadedCountStorage == chunkLoadedCount) {
                        preparationWarnings++;
                        //Guard Clause
                        if (preparationWarnings == chunkTryLimit) {
                            preparationWarnings = 0;
                            log("Terrain Preparation is taking too long! Stopping...");
                            stopBuilding(chunkLoadedCount, chunkBuildCount, (int) chunkBuildCountGoal);
                        }
                    }
                    //Same warning system but for building chunks
                    if (oldChunkBuildCountStorage == chunkBuildCount) {
                        buildingWarnings++;
                        // Guard Clause
                        if (buildingWarnings == chunkTryLimit) {
                            buildingWarnings = 0;
                            log("Terrain Building is taking too long! Stopping...");
                            stopBuilding(chunkLoadedCount, chunkBuildCount, (int) chunkBuildCountGoal);
                        }
                    }
                    //Log Warnings
                    final int spamLimit = 2;
                    if (preparationWarnings > 0) {
                        if (oldPreparationWarningCache == preparationWarnings && preparationWarnings > spamLimit) {
                            log("FL_WARN# Same prepared chunk count returned " + preparationWarnings + " time(s) in a row! Had it be " + chunkTryLimit + " time(s) in a row, chunk preparation would've stopped");
                            if (debug) logPreRendering(chunkLoadedCount);
                        }
                        if (chunkLoadedCount > oldChunkLoadedCountStorage) {
                            preparationWarnings = 0;
                        }
                    }
                    if (buildingWarnings > 0) {
                        if (oldBuildingWarningCache == buildingWarnings && buildingWarnings > spamLimit) {
                            log("FL_WARN# Same built chunk count returned " + buildingWarnings + " time(s) in a row! Had it be " + chunkTryLimit + " time(s) in a row, chunk building would've stopped");
                            if (debug) logPreRendering(chunkLoadedCount);
                        }
                        if (chunkBuildCount > oldChunkBuildCountStorage) {
                            buildingWarnings = 0;
                        }
                    }
                }
                //Next two if() statements stop building when their respective tasks are completed
                oldChunkLoadedCountStorage = chunkLoadedCount;
                oldChunkBuildCountStorage = chunkBuildCount;
                if (chunkLoadedCount >= getPreRenderArea() && chunkBuildCount >= chunkBuildCountGoal/4.0) {
                    stopBuilding(chunkLoadedCount, chunkBuildCount, (int) chunkBuildCountGoal);
                    log("Successfully prepared sufficient chunks! Stopping...");
                }
                if (chunkBuildCount >= chunkBuildCountGoal && chunkLoadedCount >= getPreRenderArea()/4.0) {
                    log("Built Sufficient Chunks! Stopping...");
                    stopBuilding(chunkLoadedCount, chunkBuildCount, (int) chunkBuildCountGoal);
                }
            }
        // Tick Timer for Pause Menu Cancellation
        } else if (timeDown < timeDownGoal) {
            timeDown++;
            if (debug) log("" + timeDown);
        }
    }
}
