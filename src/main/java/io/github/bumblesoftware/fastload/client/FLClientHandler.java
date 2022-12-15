package io.github.bumblesoftware.fastload.client;

import io.github.bumblesoftware.fastload.api.events.EventFactory;
import io.github.bumblesoftware.fastload.config.screen.BuildingTerrainScreen;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.TickTimer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.render.Camera;

import static io.github.bumblesoftware.fastload.config.init.FLMath.*;
import static io.github.bumblesoftware.fastload.events.FLClientEvents.*;

/**
 * Fastload's client handling, based upon {@link EventFactory DefaultEventFactory}.
 */
public final class FLClientHandler {
    public static void init() {
        registerEvents();
    }

    /**
     * Local client instance to access
     */
    private static final MinecraftClient client = MinecraftClient.getInstance();

    /**
     * Boolean whether an object of Player has been initialised
     */
    private static boolean playerReady = false;
    /**
     * Boolean whether player has joined ClientWorld
     */
    private static boolean playerJoined = false;
    /**
     * Checks true when the DownloadingTerrainScreen instanceof setScreen() event has been fired.
     */
    private static boolean justLoaded = false;

    /**
     * Shows render-distance difference between set value & fastload's one if there is a difference.
     * Used to send a debug message.
     */
    private static boolean showRDDOnce = false;
    /**
     * Boolean for when BuildingTerrainScreen is active
     */
    private static boolean isBuilding = false;
    /**
     * Boolean to let other methods init the process of closing the terrain building
     */
    private static boolean closeBuild = false;
    /**
     * Stores the old player camera pitch so that it can be set back to it upon completion of preloading
     */
    private static Float oldPitch = null;
    /**
     * Stores the old count to compare values on the next method call to see if the value
     * of loaded chunks is same.
     */
    private static Integer oldChunkLoadedCountStorage = null;
    /**
     * Stores the old count to compare values on the next method call to see if the value
     * of built chunks is same.
     */
    private static Integer oldChunkBuildCountStorage = null;
    /**
     * Stores the amount of warnings (or chunk tries) for terrain preparation
     */
    private static int preparationWarnings = 0;
    /**
     * Stores the amount of warnings (or chunk tries) for terrain building
     */
    private static int buildingWarnings = 0;

    /**
     * Camera instance getter
     */
    private static Camera getCamera() {
        return client.gameRenderer.getCamera();
    }

    /**
     *  Quick, easy, and lazy logging method
     */
    public static void log(String toLog) {
        Fastload.LOGGER.info(toLog);
    }

    /**
     * Client tick-based timer.
     */
    public static final TickTimer CLIENT_TIMER = new TickTimer(RENDER_TICK_EVENT);

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
                CLIENT_TIMER.setTime(20);
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

    /**
     * Event Registration for fastload
     */
    private static void registerEvents() {

        //Player is ready when it initialises
        CLIENT_PLAYER_INIT_EVENT.register(1, (eventContext, abstractEvent, closer, eventArgs) -> {
            if (getDebug()) Fastload.LOGGER.info("shouldLoad = true");
            playerReady = true;
            return null;
        });

        PLAYER_JOIN_EVENT.register(1, (eventContext, abstractEvent, closer, eventArgs) -> {
            if (getDebug()) Fastload.LOGGER.info("playerJoined = true");
            FLClientHandler.playerJoined = true;
            return null;
        });

        //Null Screen
        SET_SCREEN_EVENT.register(1, (eventContext, abstractEvent, closer, eventArgs) -> {
            if (eventContext.screen() == null) {
                isBuilding = false;
                playerReady = false;
                justLoaded = false;
                showRDDOnce = false;
                oldPitch = null;
            }
            return null;
        });

        // Game Menu Event to stop it from interfering with Fastload's stuff
        SET_SCREEN_EVENT.register(1, (eventContext, abstractEvent, closer, eventArgs) -> {
            if (CLIENT_TIMER.isReady() && eventContext.screen() instanceof GameMenuScreen && !client.windowFocused) {
                if (getDebug()) log(Integer.toString(CLIENT_TIMER.getTime()));
                eventContext.ci().cancel();
                client.setScreen(null);
            }
            return null;
        });

        //Debug when BuildingTerrainScreen is initiated
        SET_SCREEN_EVENT.register(1, (eventContext, abstractEvent, closer, eventArgs) -> {
            if (eventContext.screen() instanceof BuildingTerrainScreen && getDebug()) {
                log("Successfully Initiated Building Terrain");
            }
            return null;
        });

        //Cancels Progress screen when FORCE_CLOSE_LOADING_SCREEN is true. Makes things slightly faster
        SET_SCREEN_EVENT.register(2, (eventContext, abstractEvent, closer, eventArgs) -> {
            if (eventContext.screen() instanceof ProgressScreen && getCloseUnsafe()) {
                eventContext.ci().cancel();
                if (getDebug()) log("Progress Screen Successfully Cancelled");
            }
            return null;
        });

        //It's just magic
        SET_SCREEN_EVENT.register(1, (eventContext, abstractEvent, closer, eventArgs) -> {
            if (eventContext.screen() instanceof DownloadingTerrainScreen && playerReady && playerJoined) {
                if (getDebug()) log("Downloading Terrain Accessed!");
                playerReady = false;
                justLoaded = true;
                showRDDOnce = true;
                if (getCloseSafe()) {
                    eventContext.ci().cancel();
                    if (getDebug()) log("Preparing to replace Download Terrain with Building Terrain");
                    if (getDebug()) log("Goal (Loaded Chunks): " + getPreRenderArea());
                    isBuilding = true;
                    client.setScreen(new BuildingTerrainScreen());
                } else if (getCloseUnsafe()) {
                    playerJoined = false;
                    if (getDebug()) log("Successfully Skipped Downloading Terrain Screen!");
                    eventContext.ci().cancel();
                    client.setScreen(null);
                    CLIENT_TIMER.setTime(20);
                }
            }
            return null;
        });

        //Redundancy impl for pause menu cancellation as it this shit's unpredictable
        PAUSE_MENU_EVENT.register(1, (eventContext, abstractEvent, closer, eventArgs) -> {
            if (justLoaded) {
                if (client.windowFocused) justLoaded = false;
                else {
                    justLoaded = false;
                    eventContext.ci().cancel();
                    if (getDebug()) log("Pause Menu Cancelled");
                }
            }
            return null;
        });

        //More magic
        RENDER_TICK_EVENT.register(10, (eventContext, abstractEvent, closer, eventArgs) -> {
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
                    double FOV = client.options.fov;
                    double chunkBuildCountGoal = (FOV /180) * getPreRenderArea().doubleValue();
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

                        if ((buildingWarnings >= getChunkTryLimit() || preparationWarnings >= getChunkTryLimit()) && !getForceBuild()) {
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
                                        log("Had it be " + getChunkTryLimit() + " time(s) in a row, pre-loading would've stopped");
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
                                        log("Had it be " + getChunkTryLimit() + " time(s) in a row, pre-loading would've stopped");
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
            }
            return null;
        });
    }
}
