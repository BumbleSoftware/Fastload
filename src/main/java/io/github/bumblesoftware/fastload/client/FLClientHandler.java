package io.github.bumblesoftware.fastload.client;

import io.github.bumblesoftware.fastload.abstraction.AbstractClientCalls;
import io.github.bumblesoftware.fastload.api.event.def.CapableEvent;
import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.util.TickTimer;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.*;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.*;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.*;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Locations.SERVER_PSR_LOADING_REDIRECT;
import static io.github.bumblesoftware.fastload.config.FLMath.*;
import static io.github.bumblesoftware.fastload.init.Fastload.LOGGER;
import static io.github.bumblesoftware.fastload.init.FastloadClient.MINECRAFT_ABSTRACTION_HANDLER;

/**
 * Fastload's client handling, based upon {@link CapableEvent
 * CapableEvent}.
 */
public final class FLClientHandler {
    public static void init() {
        ifDebugEnabled(() -> LOGGER.info("FastloadClientHandler Initialised!"));
        registerEvents();
    }
    public static final AbstractClientCalls ABSTRACTED_CLIENT = MINECRAFT_ABSTRACTION_HANDLER.directory.getAbstractedEntries();
    private static Screen oldCurrentScreen = null;
    private static boolean playerReady = false;
    private static boolean playerJoined = false;
    private static Integer oldChunkLoadedCountStorage = null;
    private static Integer oldChunkBuildCountStorage = null;
    private static int preparationWarnings = 0;
    private static int buildingWarnings = 0;
    public static final TickTimer CLIENT_TIMER = new TickTimer(RENDER_TICK);

    private static void log(String toLog) {
        LOGGER.info(toLog);
    }

    private static void logRendering(int chunkLoadedCount) {
        if (ABSTRACTED_CLIENT.isSingleplayer()) {
            log("Goal (Loaded Chunks): " + getLocalRenderChunkArea());
            log("Loaded Chunks: " + chunkLoadedCount);
        } else {
            log("Goal (Loaded Chunks): " + getServerRenderChunkArea());
            log("Loaded Chunks: " + chunkLoadedCount);
        }
    }

    private static void logBuilding(int chunkBuildCount) {
        if (ABSTRACTED_CLIENT.isSingleplayer()) {
            log("Goal (Built Chunks): " + getLocalRenderChunkArea());
            log("Chunk Build Count: " + chunkBuildCount);
        } else {
            log("Goal (Built Chunks): " + getServerRenderChunkArea());
            log("Chunk Build Count: " + chunkBuildCount);
        }
    }

    private static void stopBuilding(int chunkLoadedCount, int chunkBuildCount) {
        if (playerJoined && playerReady) {
            System.gc();
            ifDebugEnabled(() -> {
                logBuilding(chunkBuildCount);
                logRendering(chunkLoadedCount);
            });
            if (!ABSTRACTED_CLIENT.isWindowFocused()) {
                CLIENT_TIMER.setTime(20);
                ifDebugEnabled(() -> log("Delaying PauseMenu until worldRendering initiates."));
            }
            playerJoined = false;
            playerReady = false;
            oldChunkLoadedCountStorage = 0;
            oldChunkBuildCountStorage = 0;
            ABSTRACTED_CLIENT.getCurrentScreen().close();
        }
    }

    private static void registerEvents() {
        EMPTY_EVENT.registerStatic(1, List.of(CLIENT_PLAYER_INIT),
                (eventContext, eventStatus, event, eventArgs) -> {
                    ifDebugEnabled(() -> LOGGER.info("shouldLoad = true"));
                    playerReady = true;
                }
        );

        PLAYER_JOIN_EVENT.registerStatic(1,
                (eventContext, eventStatus, event, eventArgs) -> {
                    ifDebugEnabled(() -> LOGGER.info("playerJoined = true"));
                    playerJoined = true;
                }
        );

        SET_SCREEN_EVENT.registerStatic(1,
                (eventContext, eventStatus, event, eventArgs) -> {
                    if (
                            CLIENT_TIMER.isReady() &&
                            ABSTRACTED_CLIENT.isGameMenuScreen(eventContext.screen()) &&
                            !ABSTRACTED_CLIENT.isWindowFocused()
                    ) {
                        ifDebugEnabled(() -> log(Integer.toString(CLIENT_TIMER.getTime())));
                        eventContext.ci().cancel();
                    }
                }
        );

        SET_SCREEN_EVENT.registerStatic(1,
                (eventContext, eventStatus, event, eventArgs) -> {
                    if (ABSTRACTED_CLIENT.isBuildingTerrainScreen(eventContext.screen())) {
                        ifDebugEnabled(() -> log("setScreen(new BuildingTerrainScreen)"));
                    }
                }
        );


        SET_SCREEN_EVENT.registerStatic(1,
                (eventContext, eventStatus, event, eventArgs) -> {
                    if (ABSTRACTED_CLIENT.isDownloadingTerrainScreen(eventContext.screen())) {
                        ifDebugEnabled(() -> log("setScreen(new DownloadingTerrainScreen)"));
                        if (playerReady && playerJoined && isInstantLoadEnabled()) {
                            eventContext.ci().cancel();
                            ABSTRACTED_CLIENT.setScreen(null);
                            playerReady = false;
                            playerJoined = false;
                            CLIENT_TIMER.setTime(20);
                        }
                    }
                }
        );

        SET_SCREEN_EVENT.registerStatic(1, List.of(LLS_441_REDIRECT),
                (eventContext, eventStatus, event, eventArgs) -> {
                    final var isPreRenderEnabled = isLocalRenderEnabled();
                    ifDebugEnabled(() ->  {
                        LOGGER.info("isLocalRenderEnabled: " + isPreRenderEnabled);
                        LOGGER.info("localRenderChunkRadius: " + getLocalRenderChunkRadius());
                        LOGGER.info("Fastload Perceived Render Distance: " + ABSTRACTED_CLIENT.getViewDistance());
                    });
                    if (isPreRenderEnabled) {
                        ABSTRACTED_CLIENT.setScreen(ABSTRACTED_CLIENT.newBuildingTerrainScreen(getLocalRenderChunkArea()));
                        ifDebugEnabled(() ->  {
                            LOGGER.info("LevelLoadingScreen -> BuildingTerrainScreen");
                            LOGGER.info("Goal (Loaded Chunks): " + getLocalRenderChunkArea());
                        });
                    } else ABSTRACTED_CLIENT.setScreen(new DownloadingTerrainScreen());
                }
        );

        SET_SCREEN_EVENT.registerStatic(1, List.of(DTS_GAME_JOIN_REDIRECT),
                (eventContext, eventStatus, event, eventArgs) -> {
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
                }
        );


        SET_SCREEN_EVENT.registerStatic(1, List.of(RESPAWN_DTS_REDIRECT),
                (eventContext, eventStatus, event, eventArgs) -> {
                    if (isInstantLoadEnabled())
                        ABSTRACTED_CLIENT.setScreen(null);
                    else ABSTRACTED_CLIENT.setScreen(eventContext.screen());
                }
        );

        SET_SCREEN_EVENT.registerStatic(1, List.of(PROGRESS_SCREEN_JOIN_WORLD_REDIRECT),
                (eventContext, eventStatus, event, eventArgs) -> {
                    if (ABSTRACTED_CLIENT.isSingleplayer()) {
                        if (isLocalRenderEnabled()) {
                            ABSTRACTED_CLIENT.reset(ABSTRACTED_CLIENT.getCurrentScreen());
                        }
                    } else if (isServerRenderEnabled()) {
                        ABSTRACTED_CLIENT.reset(ABSTRACTED_CLIENT.getCurrentScreen());
                    } else ABSTRACTED_CLIENT.reset(eventContext.screen());
                }
        );

        BOOLEAN_EVENT.registerStatic(1, List.of(DTS_TICK),
                (eventContext, eventStatus, event, eventArgs) -> {
                    eventContext.setHeldObj(true);
                    ifDebugEnabled(() ->  LOGGER.info(
                            "DownloadingTerrainScreen set to close on next render tick."
                    ));
                }
        );

        BOOLEAN_EVENT.registerStatic(1, List.of(RENDER_TICK),
               (eventContext, eventStatus, event, eventArgs) -> {
                    if (ABSTRACTED_CLIENT.forCurrentScreen(ABSTRACTED_CLIENT::isBuildingTerrainScreen)) {
                        if (ABSTRACTED_CLIENT.getClientWorld() != null) {
                            final int chunkLoadedCount = ABSTRACTED_CLIENT.getLoadedChunkCount();
                            final int chunkBuildCount = ABSTRACTED_CLIENT.getCompletedChunkCount();
                            final int oldPreparationWarningCache = preparationWarnings;
                            final int oldBuildingWarningCache = buildingWarnings;
                            final int loadingAreaGoal = ((BuildingTerrainScreen)ABSTRACTED_CLIENT.getCurrentScreen()).loadingAreaGoal;

                            ifDebugEnabled(() ->  {
                                logRendering(chunkLoadedCount);
                                logBuilding(chunkBuildCount);
                                LOGGER.info("chunkTryLimit: " + getChunkTryLimit());
                                LOGGER.info("buildingWarnings: " + buildingWarnings);
                                LOGGER.info("preparationWarnings: " + preparationWarnings);
                                LOGGER.info("chunkLoadedCount: " + oldChunkLoadedCountStorage + ":" + chunkLoadedCount);
                                LOGGER.info("chunkBuiltCount: " + oldChunkBuildCountStorage + ":" + chunkBuildCount);
                            });

                            if (oldChunkLoadedCountStorage != null && oldChunkBuildCountStorage != null
                                    && chunkBuildCount >= 0 && chunkLoadedCount >= 0
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
                                        ifDebugEnabled(() -> logRendering(chunkLoadedCount));
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
                                        ifDebugEnabled(() -> logRendering(chunkLoadedCount));
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
                }
        );

        BOOLEAN_EVENT.registerStatic(1, List.of(RENDER_TICK),
                (eventContext, eventStatus, event, eventArgs) -> FLMath.ifDebugEnabled(() ->
                        ABSTRACTED_CLIENT.forCurrentScreen(screen -> {
                        if (oldCurrentScreen != screen) {
                            oldCurrentScreen = screen;
                            LOGGER.info("Screen changed to: " + screen);
                        }
                        return false;
                }))
        );

        SERVER_EVENT.registerStatic(1, List.of(SERVER_PSR_LOADING_REDIRECT),
                (eventContext, eventStatus, event, eventArgs) -> eventContext.returnValue().setHeldObj(true)
        );

        RUNNABLE_EVENT.registerStatic(1, List.of(RP_SEND_RUNNABLE),
                (eventContext, eventStatus, event, eventArgs) -> {
                    final var client = ABSTRACTED_CLIENT.getClientInstance();
                    if (ABSTRACTED_CLIENT.forCurrentScreen(ABSTRACTED_CLIENT::isBuildingTerrainScreen))
                        ((BuildingTerrainScreen)ABSTRACTED_CLIENT.getCurrentScreen()).setClose(() ->
                                client.execute(eventContext.getHeldObj()));
                    else client.execute(eventContext.getHeldObj());
                }
        );

        BOX_BOOLEAN_EVENT.registerStatic(1, List.of(FRUSTUM_BOX_BOOL),
                (eventContext, eventStatus, event, eventArgs) -> {
                    if (ABSTRACTED_CLIENT.forCurrentScreen(ABSTRACTED_CLIENT::isBuildingTerrainScreen) ||
                            ABSTRACTED_CLIENT.forCurrentScreen(ABSTRACTED_CLIENT::isDownloadingTerrainScreen)
                    )
                        eventContext.cir().setReturnValue(true);
                }
        );

        INTEGER_EVENT.registerStatic(1, List.of(WORLD_ICON),
                (eventContext, eventStatus, event, eventArgs) -> {
                    eventContext.setHeldObj(100);
                    ifDebugEnabled(() -> LOGGER.info("worldIcon time prolonged"));
                }
        );
    }
}
