package io.github.bumblesoftware.fastload.client;

import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.ObjectHolder;
import io.github.bumblesoftware.fastload.util.TickTimer;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.PLAYER_JOIN_EVENT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.SET_SCREEN_EVENT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.*;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.*;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Locations.SERVER_PSR_LOADING_REDIRECT;
import static io.github.bumblesoftware.fastload.config.FLMath.*;
import static io.github.bumblesoftware.fastload.init.Fastload.LOGGER;
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


    /**
     * Boolean whether an object of Player has been initialised
     */
    private static boolean playerReady = false;
    /**
     * Boolean whether player has joined ClientWorld
     */
    private static boolean playerJoined = false;
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
     * Client bool-based timer.
     */
    public static final TickTimer CLIENT_TIMER = new TickTimer(RENDER_TICK);

    /**
     * Logs amount of prepared chunks;
     */
    private static void logRendering(int chunkLoadedCount) {
        if (ABSTRACTED_CLIENT.isSingleplayer()) {
            log("Goal (Loaded Chunks): " + getLocalRenderChunkArea());
            log("Loaded Chunks: " + chunkLoadedCount);
        } else {
            log("Goal (Loaded Chunks): " + getServerRenderChunkArea());
            log("Loaded Chunks: " + chunkLoadedCount);
        }
    }

    /**
     * Lots Chunk-building status
     */
    private static void logBuilding(int chunkBuildCount) {
        if (ABSTRACTED_CLIENT.isSingleplayer()) {
            log("Goal (Built Chunks): " + getLocalRenderChunkArea());
            log("Chunk Build Count: " + chunkBuildCount);
        } else {
            log("Goal (Built Chunks): " + getServerRenderChunkArea());
            log("Chunk Build Count: " + chunkBuildCount);
        }
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
            ABSTRACTED_CLIENT.getCurrentScreen().close();
        }
    }

    /**
     * Event Registration for fastload
     */
    private static void registerEvents() {
        EMPTY_EVENT.registerThreadUnsafe(1, List.of(CLIENT_PLAYER_INIT),
                 event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (isDebugEnabled()) Fastload.LOGGER.info("shouldLoad = true");
                    playerReady = true;
                })
        );

        PLAYER_JOIN_EVENT.registerThreadUnsafe(1,
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (isDebugEnabled()) Fastload.LOGGER.info("playerJoined = true");
                    playerJoined = true;
                })
        );

        SET_SCREEN_EVENT.registerThreadUnsafe(1,
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (CLIENT_TIMER.isReady() &&
                            ABSTRACTED_CLIENT.isGameMenuScreen(eventContext.screen()) &&
                            !ABSTRACTED_CLIENT.isWindowFocused()
                    ) {
                        if (isDebugEnabled()) log(Integer.toString(CLIENT_TIMER.getTime()));
                        eventContext.ci().cancel();
                    }
                })
        );

        SET_SCREEN_EVENT.registerThreadUnsafe(1,
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (ABSTRACTED_CLIENT.isBuildingTerrainScreen(eventContext.screen())) {
                        if (isDebugEnabled())
                            log("setScreen(new BuildingTerrain)");
                    }
                })
        );


        SET_SCREEN_EVENT.registerThreadUnsafe(1,
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (ABSTRACTED_CLIENT.isDownloadingTerrainScreen(eventContext.screen())) {
                        if (isDebugEnabled())
                            log("setScreen(new DownloadingTerrainScreen)");
                        if (playerReady && playerJoined && isInstantLoadEnabled()) {
                            eventContext.ci().cancel();
                            ABSTRACTED_CLIENT.getClientInstance().setScreen(null);
                            playerReady = false;
                            playerJoined = false;
                            CLIENT_TIMER.setTime(20);
                        }
                    }
                })
        );

        SET_SCREEN_EVENT.registerThreadUnsafe(1, List.of(LLS_441_REDIRECT),
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    final var isPreRenderEnabled = isLocalRenderEnabled();
                    if (isDebugEnabled()) {
                        LOGGER.info("isLocalRenderEnabled: " + isPreRenderEnabled);
                        LOGGER.info("localRenderChunkRadius: " + getLocalRenderChunkRadius());
                        LOGGER.info("Fastload Perceived Render Distance: " + ABSTRACTED_CLIENT.getViewDistance());
                    }
                    if (isPreRenderEnabled) {
                        ABSTRACTED_CLIENT.setScreen(ABSTRACTED_CLIENT.newBuildingTerrainScreen(getLocalRenderChunkArea()));
                        if (isDebugEnabled()) {
                            LOGGER.info("LevelLoadingScreen -> BuildingTerrainScreen");
                            LOGGER.info("Goal (Loaded Chunks): " + getLocalRenderChunkArea());
                        }
                    } else ABSTRACTED_CLIENT.setScreen(new DownloadingTerrainScreen());
                })
        );

        SET_SCREEN_EVENT.registerThreadUnsafe(1, List.of(DTS_GAME_JOIN_REDIRECT),
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (ABSTRACTED_CLIENT.isSingleplayer()) {
                        if (!isLocalRenderEnabled())
                            ABSTRACTED_CLIENT.setScreen(eventContext.screen());
                    } else {
                        if (isServerRenderEnabled())
                            ABSTRACTED_CLIENT.setScreen(ABSTRACTED_CLIENT.newBuildingTerrainScreen(getServerRenderChunkArea()));
                        else {
                            if (isInstantLoadEnabled())
                                ABSTRACTED_CLIENT.setScreen(null);
                            else ABSTRACTED_CLIENT.setScreen(eventContext.screen());
                        }
                    }
                }));

        BOOLEAN_EVENT.registerThreadUnsafe(1, List.of(RENDER_TICK),
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (ABSTRACTED_CLIENT.forCurrentScreen(ABSTRACTED_CLIENT::isBuildingTerrainScreen)) {
                        if (ABSTRACTED_CLIENT.getClientWorld() != null) {
                            final int chunkLoadedCount = ABSTRACTED_CLIENT.getLoadedChunkCount();
                            final int chunkBuildCount = ABSTRACTED_CLIENT.getCompletedChunkCount();
                            final int oldPreparationWarningCache = preparationWarnings;
                            final int oldBuildingWarningCache = buildingWarnings;
                            final int loadingAreaGoal = ((BuildingTerrainScreen)ABSTRACTED_CLIENT.getCurrentScreen()).loadingAreaGoal;

                            if (isDebugEnabled()) {
                                logRendering(chunkLoadedCount);
                                logBuilding(chunkBuildCount);
                            }

                            if (oldChunkLoadedCountStorage != null && oldChunkBuildCountStorage != null
                                    && chunkBuildCount > 0 && chunkLoadedCount > 0
                            ) {
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

                            if (chunkLoadedCount >= loadingAreaGoal && chunkBuildCount >= loadingAreaGoal) {
                                stopBuilding(chunkLoadedCount, chunkBuildCount);
                                log("Successfully pre-loaded the world!");
                            }
                        }
                    }
                })
        );

        BOOLEAN_EVENT.registerThreadUnsafe(1, List.of(RENDER_TICK),
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (isDebugEnabled()) {
                        ABSTRACTED_CLIENT.forCurrentScreen(screen -> {
                            if (oldCurrentScreen != screen) {
                                oldCurrentScreen = screen;
                                Fastload.LOGGER.info("Screen changed to: " + screen);
                            }
                            return false;
                        });
                    }
                })
        );

        //noinspection RedundantCast,unchecked
        SERVER_EVENT.registerThreadUnsafe(1, List.of(SERVER_PSR_LOADING_REDIRECT),
                event -> event.stableArgs((eventContext, eventArgs) ->
                        ((ObjectHolder<Boolean>)eventContext.returnValue()).heldObj = true
                )
        );

        SET_SCREEN_EVENT.registerThreadUnsafe(1, List.of(RESPAWN_DTS_REDIRECT),
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (!isInstantLoadEnabled())
                        ABSTRACTED_CLIENT.setScreen(eventContext.screen());
                })
        );
    }
}
