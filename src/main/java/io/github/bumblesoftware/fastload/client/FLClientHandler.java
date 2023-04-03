package io.github.bumblesoftware.fastload.client;

import io.github.bumblesoftware.fastload.config.screen.BuildingTerrainScreen;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.TickTimer;
import net.minecraft.client.MinecraftClient;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.getPreRenderArea;
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

/**
 * Fastload's client handling, based upon {@link io.github.bumblesoftware.fastload.api.events.CapableEvent
 * CapableEvent}.
 */
public final class FLClientHandler {
    public static void init() {
        registerEvents();
    }

    /**
     * Local client instance to access
     */
    private static final MinecraftClient client = ABSTRACTED_CLIENT.getClientInstance();

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
    private static void logBuilding(int chunkBuildCount) {
        log("Goal (Built Chunks): " + getPreRenderArea());
        log("Chunk Build Count: " + chunkBuildCount);
    }

    /**
     * Stops the BuildingTerrainScreen when called and resets relevant params
     */
    private static void stopBuilding(int chunkLoadedCount, int chunkBuildCount) {
        if (playerJoined) {
            closeBuild = true;
            if (isDebugEnabled()) {
                logBuilding(chunkBuildCount);
                logPreRendering(chunkLoadedCount);
            }
            isBuilding = false;
            if (!ABSTRACTED_CLIENT.isWindowFocused()) {
                CLIENT_TIMER.setTime(20);
                if (isDebugEnabled()) log("Temporarily Cancelling Pause Menu to enable Renderer");
            }
            assert client.player != null;
            if (oldPitch != null) {
                ABSTRACTED_CLIENT.setPlayerRotation(ABSTRACTED_CLIENT.getPlayerYaw(), oldPitch);
                if (ABSTRACTED_CLIENT.getPlayerPitch() != oldPitch) ABSTRACTED_CLIENT.setPlayerPitch(oldPitch);
                oldPitch = null;
            }
            playerJoined = false;
            oldChunkLoadedCountStorage = 0;
            oldChunkBuildCountStorage = 0;
            System.gc();
            ABSTRACTED_CLIENT.setScreen(null);
        }
    }

    /**
     * Event Registration for fastload
     */
    private static void registerEvents() {

        //Player is ready when it initialises
        CLIENT_PLAYER_INIT_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (isDebugEnabled()) Fastload.LOGGER.info("shouldLoad = true");
            playerReady = true;
            return null;
        });

        PLAYER_JOIN_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (isDebugEnabled()) Fastload.LOGGER.info("playerJoined = true");
            FLClientHandler.playerJoined = true;
            return null;
        });

        //Null Screen
        SET_SCREEN_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
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
        SET_SCREEN_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (CLIENT_TIMER.isReady() && ABSTRACTED_CLIENT.isGameMenuScreen(eventContext.screen()) && !ABSTRACTED_CLIENT.isWindowFocused()) {
                if (isDebugEnabled()) log(Integer.toString(CLIENT_TIMER.getTime()));
                eventContext.ci().cancel();
                ABSTRACTED_CLIENT.setScreen(null);
            }
            return null;
        });

        //Debug when BuildingTerrainScreen is initiated
        SET_SCREEN_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (eventContext.screen() instanceof BuildingTerrainScreen && isDebugEnabled()) {
                log("Successfully Initiated Building Terrain");
            }
            return null;
        });

        //Cancels Progress screen when FORCE_CLOSE_LOADING_SCREEN is true. Makes things slightly faster
        SET_SCREEN_EVENT.registerThreadUnsafe(2, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (ABSTRACTED_CLIENT.isProgressScreen(eventContext.screen()) && isForceCloseEnabled()) {
                eventContext.ci().cancel();
                if (isDebugEnabled()) log("Progress Screen Successfully Cancelled");
            }
            return null;
        });

        //It's just magic
        SET_SCREEN_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (ABSTRACTED_CLIENT.isDownloadingTerrainScreen(eventContext.screen()) && playerReady && playerJoined) {
                if (isDebugEnabled()) log("Downloading Terrain Accessed!");
                playerReady = false;
                justLoaded = true;
                showRDDOnce = true;
                if (isPreRenderEnabled()) {
                    eventContext.ci().cancel();
                    if (isDebugEnabled()) log("Preparing to replace Download Terrain with Building Terrain");
                    if (isDebugEnabled()) log("Goal (Loaded Chunks): " + getPreRenderArea());
                    isBuilding = true;
                    System.gc();
                    ABSTRACTED_CLIENT.setScreen(new BuildingTerrainScreen());
                } else if (isForceCloseEnabled()) {
                    playerJoined = false;
                    if (isDebugEnabled()) log("Successfully Skipped Downloading Terrain Screen!");
                    eventContext.ci().cancel();
                    ABSTRACTED_CLIENT.setScreen(null);
                    CLIENT_TIMER.setTime(20);
                }
            }
            return null;
        });

        //Redundancy impl for pause menu cancellation as it this shit's unpredictable
        PAUSE_MENU_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (justLoaded) {
                if (ABSTRACTED_CLIENT.isWindowFocused()) justLoaded = false;
                else {
                    justLoaded = false;
                    eventContext.ci().cancel();
                    if (isDebugEnabled()) log("Pause Menu Cancelled");
                }
            }
            return null;
        });

        //More magic
        RENDER_TICK_EVENT.registerThreadUnsafe(10, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            // Logs render distance
            if (showRDDOnce) {
                logRenderDistanceDifference();
                showRDDOnce = false;
            }
            //Pre-rendering Engine
            if (isBuilding) {
                if (ABSTRACTED_CLIENT.getClientWorld() != null) {
                    final int chunkLoadedCount = ABSTRACTED_CLIENT.getLoadedChunkCount();
                    final int chunkBuildCount = ABSTRACTED_CLIENT.getCompletedChunkCount();
                    final int oldPreparationWarningCache = preparationWarnings;
                    final int oldBuildingWarningCache = buildingWarnings;

                    if (isDebugEnabled()) {
                        logPreRendering(chunkLoadedCount);
                        logBuilding(chunkBuildCount);
                    }
                    //The warning system
                    if (oldChunkLoadedCountStorage != null && oldChunkBuildCountStorage != null) {
                        if (oldChunkLoadedCountStorage == chunkLoadedCount)
                            preparationWarnings++;
                        if (oldChunkBuildCountStorage == chunkBuildCount)
                            buildingWarnings++;

                        if ((buildingWarnings >= getChunkTryLimit() || preparationWarnings >= getChunkTryLimit()) && !isForceBuildEnabled()) {
                            buildingWarnings = 0;
                            preparationWarnings = 0;
                            log("Pre-loading is taking too long! Stopping...");
                            stopBuilding(chunkLoadedCount, chunkBuildCount);
                        }

                        if (!closeBuild) {
                            //Log Warnings
                            final int spamLimit = 2;
                            if (preparationWarnings > 0) {
                                if (oldPreparationWarningCache == preparationWarnings && preparationWarnings > spamLimit) {
                                    log("FL_WARN# Same prepared chunk count returned " + preparationWarnings + " time(s) in a row!");
                                    if (!isForceBuildEnabled()) {
                                        log("Had it be " + getChunkTryLimit() + " time(s) in a row, pre-loading would've stopped");
                                    }
                                    if (isDebugEnabled()) logPreRendering(chunkLoadedCount);
                                }
                                if (chunkLoadedCount > oldChunkLoadedCountStorage) {
                                    preparationWarnings = 0;
                                }
                            }
                            if (buildingWarnings > 0) {
                                if (oldBuildingWarningCache == buildingWarnings && buildingWarnings > spamLimit) {
                                    log("FL_WARN# Same built chunk count returned " + buildingWarnings + " time(s) in a row");
                                    if (!isForceBuildEnabled()) {
                                        log("Had it be " + getChunkTryLimit() + " time(s) in a row, pre-loading would've stopped");
                                    }
                                    if (isDebugEnabled()) logPreRendering(chunkLoadedCount);
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

                    if (chunkLoadedCount >= getPreRenderArea() && chunkBuildCount >= getPreRenderArea()) {
                        stopBuilding(chunkLoadedCount, chunkBuildCount);
                        log("Successfully pre-loaded the world! Stopping...");
                    }
                }
            }
            return null;
        });
    }
}
