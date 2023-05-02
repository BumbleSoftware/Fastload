package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.api.external.abstraction.core.handler.AbstractionHandler;
import io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning.VersionConstants;
import io.github.bumblesoftware.fastload.api.internal.abstraction.AbstractClientCalls;
import io.github.bumblesoftware.fastload.api.internal.abstraction.Client1182;
import io.github.bumblesoftware.fastload.client.FLClientEvents;
import io.github.bumblesoftware.fastload.client.FLClientHandler;
import io.github.bumblesoftware.fastload.config.FLConfig;
import io.github.bumblesoftware.fastload.config.FLMath;
import net.fabricmc.api.ClientModInitializer;

import java.util.List;

import static io.github.bumblesoftware.fastload.api.external.abstraction.core.handler.AbstractionFactory.create;
import static io.github.bumblesoftware.fastload.api.external.abstraction.core.handler.AbstractionHandler.Environment.CLIENT;
import static io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning.VersionConstants.IS_MINECRAFT_1182;
import static io.github.bumblesoftware.fastload.config.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.config.FLMath.*;
import static io.github.bumblesoftware.fastload.init.Fastload.LOGGER;

public class FastloadClient implements ClientModInitializer {
    public static final AbstractionHandler<AbstractClientCalls> MINECRAFT_ABSTRACTION_HANDLER;

    static {
        VersionConstants.init();
        MINECRAFT_ABSTRACTION_HANDLER = create(
                List.of("fastload-119-0-1-2-compat", "fastload-1193-compat", "fastload-1194-compat"),
                CLIENT,
                event -> event.registerThreadUnsafe(0,
                        eventInstance -> event.stableArgs((eventContext, eventArgs) -> {
                            if (IS_MINECRAFT_1182) {
                                if (FLMath.isDebugEnabled())
                                    Fastload.LOGGER.info("Fastload 1.18.2 Base!");
                                eventContext.heldObj = new Client1182();
                            }
                        })
                )
        );
        FLConfig.init();
        FLClientEvents.init();
        FLClientHandler.init();

        LOGGER.info("Fastload Perceived Version: " + MINECRAFT_ABSTRACTION_HANDLER.directory.getVersion());
        LOGGER.info("Fastload Abstraction Supported Versions: " + MINECRAFT_ABSTRACTION_HANDLER.directory.getSupportedVersionsNonArray());
        LOGGER.info(logKey(DEBUG_KEY) + isDebugEnabled().toString().toUpperCase());
        LOGGER.info(logKey(CHUNK_TRY_LIMIT_KEY) + getChunkTryLimit());
        LOGGER.info(logKey(LOCAL_RENDER_RADIUS_KEY) + getLocalRenderChunkRadius());
        LOGGER.info(logKey(LOCAL_RENDER_AREA_KEY) + getLocalRenderChunkArea());
        LOGGER.info(logKey(SERVER_RENDER_RADIUS_KEY) + getServerRenderChunkRadius());
        LOGGER.info(logKey(SERVER_RENDER_AREA_KEY) + getServerRenderChunkArea());
    }

    @Override
    public void onInitializeClient() {}

    private static String logKey(String key) {
        return key.toUpperCase() + ": ";
    }
}
