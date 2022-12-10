package io.github.bumblesoftware.fastload.client;

import io.github.bumblesoftware.fastload.api.events.FLEvents;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.config.screen.BuildingTerrainScreen;
import io.github.bumblesoftware.fastload.init.FastLoad;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.render.Camera;

import static io.github.bumblesoftware.fastload.config.init.FLMath.*;

public final class FLClientHandler {
    public static void init() {}
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static boolean shouldLoad = false;
    public static boolean playerJoined = false;

    private static boolean justLoaded = false;
    private static boolean showRDDOnce = false;
    //Boolean  Pre-render
    private static boolean isBuilding = false;
    private static boolean closeBuild = false;
    //Pre Renderer Log Constants
    @SuppressWarnings("FieldCanBeLocal")
    private static final int chunkTryLimit = getChunkTryLimit();
    //Storage
    private static Float oldPitch = null;
    private static Integer oldChunkLoadedCountStorage = null;
    private static Integer oldChunkBuildCountStorage = null;
    //Warning Constants
    private static int preparationWarnings = 0;
    private static int buildingWarnings = 0;
    //Ticks until Pause Menu is Active again
    private static final int timeDownGoal = 10;
    // Set this to 0 to start timer for Pause Menu Cancellation
    private static int timeDown = timeDownGoal;

    /**
     * It gets the instance of the players camera
     */
    private static Camera getCamera() {
        return client.gameRenderer.getCamera();
    }

    /**
     *  Quick, easy, and lazy logging method
     */
    private static void log(String toLog) {
        FastLoad.LOGGER.info(toLog);
    }

    /**
     * Logs Difference in Render and Pre-render distances
     */
    private static void logRenderDistanceDifference() {
        if (!getPreRenderRadius().equals(getPreRenderRadius(true)))
            log("Pre-rendering radius changed to "
                    + getPreRenderRadius() + " from " + getPreRenderRadius(true)
                    + " to protect from chunks not loading past your given render distance. " +
                    "To resolve this, please adjust your render distance accordingly");
    }

    /**
     * Logs amount of prepared chunks;
     */
    private static void logPreRendering(int chunkLoadedCount) {
        log("Goal (Loaded Chunks): " + getPreRenderArea());
        log("Loaded Chunks: " + chunkLoadedCount);
    }

    /**
     * Lots Chunk-building status
     */
    private static void logBuilding(int chunkBuildCount, int chunkBuildCountGoal) {
        log("Goal (Built Chunks): " + chunkBuildCountGoal);
        log("Chunk Build Count: " + chunkBuildCount);
    }

    /**
     * Stops the BuildingTerrainScreen when called and resets relevant params
     */
    private static void stopBuilding(int chunkLoadedCount, int chunkBuildCount, int chunkBuildCountGoal) {
        if (playerJoined) {
            closeBuild = true;
            if (getDebug()) {
                logBuilding(chunkBuildCount, chunkBuildCountGoal);
                logPreRendering(chunkLoadedCount);
            }
            isBuilding = false;
            if (!client.windowFocused) {
                timeDown = 0;
                if (getDebug()) log("Temporarily Cancelling Pause Menu to enable Renderer");
            }
            assert client.player != null;
            if (oldPitch != null) {
                getCamera().setRotation(client.player.getYaw(), oldPitch);
                if (client.player.getPitch() != oldPitch) client.player.setPitch(oldPitch);
                oldPitch = null;
            }
            playerJoined = false;
            oldChunkLoadedCountStorage = 0;
            oldChunkBuildCountStorage = 0;
            client.setScreen(null);
        }
    }

    static {

        //Null Screen
        FLEvents.SET_SCREEN_EVENT.register((screen, ci) -> {
            if (screen == null) {
                isBuilding = false;
                shouldLoad = false;
                justLoaded = false;
                showRDDOnce = false;
                oldPitch = null;
            }
        });

        // Game Menu Event to stop it from interfering with Fastload's stuff
        FLEvents.SET_SCREEN_EVENT.register((screen, ci) -> {
            if (timeDown < timeDownGoal && screen instanceof GameMenuScreen && !client.windowFocused) {
                ci.cancel();
                client.setScreen(null);
            }
        });

        //Debug when BuildingTerrainScreen is initiated
        FLEvents.SET_SCREEN_EVENT.register((screen, ci) -> {
            if (screen instanceof BuildingTerrainScreen && getDebug()) {
                log("Successfully Initiated Building Terrain");
            }
        });

        FLEvents.SET_SCREEN_EVENT.register((screen, ci) -> {
            if (screen instanceof ProgressScreen && getCloseUnsafe()) {
                ci.cancel();
                if (getDebug()) log("Progress Screen Successfully Cancelled");
            }
        });

        FLEvents.SET_SCREEN_EVENT.register((screen, ci) -> {
            if (screen instanceof DownloadingTerrainScreen && shouldLoad && playerJoined) {
                if (getDebug()) log("Downloading Terrain Accessed!");
                shouldLoad = false;
                justLoaded = true;
                showRDDOnce = true;
                if (getCloseSafe()) {
                    ci.cancel();
                    if (getDebug()) log("Preparing to replace Download Terrain with Building Terrain");
                    if (getDebug()) log("Goal (Loaded Chunks): " + getPreRenderArea());
                    isBuilding = true;
                    client.setScreen(new BuildingTerrainScreen());
                } else if (getCloseUnsafe()) {
                    playerJoined = false;
                    ci.cancel();
                    if (getDebug()) log("Successfully Skipped Downloading Terrain Screen!");
                    timeDown = 0;
                    client.setScreen(null);
                }
            }
        });

        FLEvents.PAUSE_MENU_EVENT.register((pause, ci) -> {
            if (justLoaded) {
                if (client.windowFocused) justLoaded = false;
                else {
                    justLoaded = false;
                    ci.cancel();
                    if (getDebug()) log("Pause Menu Cancelled");
                }
            }
        });

        FLEvents.RENDER_TICK_EVENT.register((tick, ci) -> {
            // Logs render distance
            if (showRDDOnce) {
                logRenderDistanceDifference();
                showRDDOnce = false;
            }
            //Pre-rendering Engine
            if (isBuilding) {
                if (client.world != null) {
                    //Sets player to face horizontally to prioritise chunk loading
                    if (client.player != null) {
                        if (oldPitch == null) {
                            oldPitch = client.player.getPitch();
                        }
                        client.player.setPitch(0);
                        if (getDebug()) {
                            log("Pitch:" + oldPitch);
                        }
                    }

                    int chunkLoadedCount = client.world.getChunkManager().getLoadedChunkCount();
                    int chunkBuildCount = client.worldRenderer.getCompletedChunkCount();
                    double FOV = client.options.getFov().getValue();
                    double chunkBuildCountGoal = (FOV/360) * getPreRenderArea().doubleValue();
                    final int oldPreparationWarningCache = preparationWarnings;
                    final int oldBuildingWarningCache = buildingWarnings;

                    if (getDebug()) {
                        logPreRendering(chunkLoadedCount);
                        logBuilding(chunkBuildCount, (int) chunkBuildCountGoal);
                    }
                    //The warning system
                    if (oldChunkLoadedCountStorage != null && oldChunkBuildCountStorage != null) {
                        if (oldChunkLoadedCountStorage == chunkLoadedCount)
                            preparationWarnings++;
                        if (oldChunkBuildCountStorage == chunkBuildCount)
                            buildingWarnings++;

                        if ((buildingWarnings >= chunkTryLimit || preparationWarnings >= chunkTryLimit) && !FLMath.getForceBuild()) {
                            buildingWarnings = 0;
                            preparationWarnings = 0;
                            log("Pre-loading is taking too long! Stopping...");
                            stopBuilding(chunkLoadedCount, chunkBuildCount, (int) chunkBuildCountGoal);
                        }

                        if (!closeBuild) {
                            //Log Warnings
                            final int spamLimit = 2;
                            if (preparationWarnings > 0) {
                                if (oldPreparationWarningCache == preparationWarnings && preparationWarnings > spamLimit) {
                                    log("FL_WARN# Same prepared chunk count returned " + preparationWarnings + " time(s) in a row!");
                                    if (!getForceBuild()) {
                                        log("Had it be " + chunkTryLimit + " time(s) in a row, pre-loading would've stopped");
                                    }
                                    if (getDebug()) logPreRendering(chunkLoadedCount);
                                }
                                if (chunkLoadedCount > oldChunkLoadedCountStorage) {
                                    preparationWarnings = 0;
                                }
                            }
                            if (buildingWarnings > 0) {
                                if (oldBuildingWarningCache == buildingWarnings && buildingWarnings > spamLimit) {
                                    log("FL_WARN# Same built chunk count returned " + buildingWarnings + " time(s) in a row");
                                    if (!getForceBuild()) {
                                        log("Had it be " + chunkTryLimit + " time(s) in a row, pre-loading would've stopped");
                                    }
                                    if (getDebug()) logPreRendering(chunkLoadedCount);
                                }
                                if (chunkBuildCount > oldChunkBuildCountStorage) {
                                    buildingWarnings = 0;
                                }
                            }
                        }
                    }

                    //Stops when completed

                    oldChunkLoadedCountStorage = chunkLoadedCount;
                    oldChunkBuildCountStorage = chunkBuildCount;

                    if (chunkLoadedCount >= getPreRenderArea() && chunkBuildCount >= chunkBuildCountGoal) {
                        stopBuilding(chunkLoadedCount, chunkBuildCount, (int) chunkBuildCountGoal);
                        log("Successfully pre-loaded the world! Stopping...");
                    }
                }
                // Tick Timer for Pause Menu Cancellation
            } else if (timeDown < timeDownGoal) {
                timeDown++;
                if (getDebug()) log("" + timeDown);
            }
        });
    }
}
