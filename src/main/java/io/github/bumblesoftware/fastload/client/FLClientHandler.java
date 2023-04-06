package io.github.bumblesoftware.fastload.client;

import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.TickTimer;
import net.minecraft.client.gui.screen.Screen;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

/**
 * Fastload's client handling, based upon {@link io.github.bumblesoftware.fastload.api.events.CapableEvent
 * CapableEvent}.
 */
public final class FLClientHandler {

    public static void init() {
        registerEvents();
    }

    private static Screen oldCurrentScreen = null;

    private static boolean accessedDownloadingTerrainScreen = false;


    /**
     * Boolean whether an object of Player has been initialised
     */
    private static boolean playerReady = false;
    /**
     * Boolean whether player has joined ClientWorld
     */
    private static boolean playerJoined = false;

    /**
     * Shows getRenderKey-distance difference between set value & fastload's one if there is a difference.
     * Used to send a debug message.
     */
    private static boolean hasNotShownRenderDifference = false;
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
     * Logs Difference in Render and Pre-getRenderKey distances
     */
    private static void logRenderDistanceDifference() {
        if (getRenderChunkRadius() != getRenderChunkRadius(true))
            log("Pre-rendering radius changed to "
                    + getRenderChunkRadius() + " from " + getRenderChunkRadius(true)
                    + " to protect from chunks not loading past your given getRenderKey distance. " +
                    "To resolve this, please adjust your getRenderKey distance accordingly");
    }

    /**
     * Logs amount of prepared chunks;
     */
    private static void logRendering(int chunkLoadedCount) {
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
            System.gc();
            if (isDebugEnabled()) {
                logBuilding(chunkBuildCount);
                logRendering(chunkLoadedCount);
            }
            if (!ABSTRACTED_CLIENT.isWindowFocused()) {
                CLIENT_TIMER.setTime(20);
                if (isDebugEnabled()) log("Delaying PauseMenu until worldRendering initiates.");
            }
            playerJoined = false;
            oldChunkLoadedCountStorage = 0;
            oldChunkBuildCountStorage = 0;
            ABSTRACTED_CLIENT.setScreen(null);
        }
    }

    /**
     * Event Registration for fastload
     */
    private static void registerEvents() {
        CLIENT_PLAYER_INIT_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (isDebugEnabled()) Fastload.LOGGER.info("shouldLoad = true");
            playerReady = true;
            return null;
        });

        PLAYER_JOIN_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (isDebugEnabled()) Fastload.LOGGER.info("playerJoined = true");
            playerJoined = true;
            return null;
        });

        SET_SCREEN_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (CLIENT_TIMER.isReady() && ABSTRACTED_CLIENT.isGameMenuScreen(eventContext.screen()) && !ABSTRACTED_CLIENT.isWindowFocused()) {
                if (isDebugEnabled()) log(Integer.toString(CLIENT_TIMER.getTime()));
                eventContext.ci().cancel();
                ABSTRACTED_CLIENT.setScreen(null);
            }
            return null;
        });

        SET_SCREEN_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
                if (ABSTRACTED_CLIENT.isBuildingTerrainScreen(eventContext.screen())) {
                    hasNotShownRenderDifference = true;
                    if (isDebugEnabled())
                        log("setScreen(new BuildingTerrain)");
                }
            return null;
        });


        SET_SCREEN_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (ABSTRACTED_CLIENT.isDownloadingTerrainScreen(eventContext.screen()) && playerReady && playerJoined) {
                if (isDebugEnabled()) log("setScreen(new DownloadingTerrainScreen)");
                playerReady = false;
                playerJoined = false;
                accessedDownloadingTerrainScreen = true;
            }
            return null;
        });

        SET_SCREEN_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (eventContext.screen() == null && accessedDownloadingTerrainScreen) {
                CLIENT_TIMER.setTime(20);
                accessedDownloadingTerrainScreen = false;
            }
            return null;
        });

        RENDER_TICK_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (hasNotShownRenderDifference) {
                logRenderDistanceDifference();
                hasNotShownRenderDifference = false;
            }
            if (ABSTRACTED_CLIENT.forCurrentScreen(ABSTRACTED_CLIENT::isBuildingTerrainScreen)) {
                if (ABSTRACTED_CLIENT.getClientWorld() != null) {
                    final int chunkLoadedCount = ABSTRACTED_CLIENT.getLoadedChunkCount();
                    final int chunkBuildCount = ABSTRACTED_CLIENT.getCompletedChunkCount();
                    final int oldPreparationWarningCache = preparationWarnings;
                    final int oldBuildingWarningCache = buildingWarnings;

                    if (isDebugEnabled()) {
                        logRendering(chunkLoadedCount);
                        logBuilding(chunkBuildCount);
                    }

                    if (oldChunkLoadedCountStorage != null && oldChunkBuildCountStorage != null) {
                        if (oldChunkLoadedCountStorage == chunkLoadedCount)
                            preparationWarnings++;
                        if (oldChunkBuildCountStorage == chunkBuildCount)
                            buildingWarnings++;

                        if ((buildingWarnings >= getChunkTryLimit() || preparationWarnings >= getChunkTryLimit())) {
                            buildingWarnings = 0;
                            preparationWarnings = 0;
                            log("Rendering is either taking too long or hit a roadblock. If you are in a server, this" +
                                    " is potentially a limitation of the servers render distance and can be ignored.");
                            stopBuilding(chunkLoadedCount, chunkBuildCount);
                        }

                        //Log Warnings
                        final int spamLimit = 2;
                        if (preparationWarnings > 0) {
                            if (oldPreparationWarningCache == preparationWarnings && preparationWarnings > spamLimit) {
                                log("Same prepared chunk count returned " + preparationWarnings + " time(s) in a row!");
                                log("Had it be " + getChunkTryLimit() + " time(s) in a row, rendering would've " +
                                        "stopped");
                                if (isDebugEnabled()) logRendering(chunkLoadedCount);
                            }
                            if (chunkLoadedCount > oldChunkLoadedCountStorage) {
                                preparationWarnings = 0;
                            }
                        }
                        if (buildingWarnings > 0) {
                            if (oldBuildingWarningCache == buildingWarnings && buildingWarnings > spamLimit) {
                                log("Same built chunk count returned " + buildingWarnings + " time(s) in a row!");
                                log("Had it be " + getChunkTryLimit() + " time(s) in a row, rendering would've " +
                                        "stopped");
                                if (isDebugEnabled()) logRendering(chunkLoadedCount);
                            }
                            if (chunkBuildCount > oldChunkBuildCountStorage) {
                                buildingWarnings = 0;
                            }
                        }
                    }

                    oldChunkLoadedCountStorage = chunkLoadedCount;
                    oldChunkBuildCountStorage = chunkBuildCount;

                    if (chunkLoadedCount >= getPreRenderArea() && chunkBuildCount >= getPreRenderArea()) {
                        stopBuilding(chunkLoadedCount, chunkBuildCount);
                        log("Successfully pre-loaded the world!");
                    }
                }
            }
            return null;
        });

        RENDER_TICK_EVENT.registerThreadUnsafe(1, (eventContext, abstractUnsafeEvent, closer, eventArgs) -> {
            if (FLMath.isDebugEnabled()) {
                ABSTRACTED_CLIENT.forCurrentScreen(screen -> {
                    if (oldCurrentScreen != screen) {
                        oldCurrentScreen = screen;
                        Fastload.LOGGER.info(screen.getTitle().getString() + "--" + screen);
                    }
                    return false;
                });
            }
            return null;
        });
    }
}
