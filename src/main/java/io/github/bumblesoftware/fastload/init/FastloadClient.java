package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.api.external.abstraction.client.AbstractClientCalls;
import io.github.bumblesoftware.fastload.api.external.abstraction.client.AbstractionManagerImpl;
import io.github.bumblesoftware.fastload.api.external.abstraction.management.AbstractionHandler;
import io.github.bumblesoftware.fastload.api.external.abstraction.tool.version.AbstractionManager;
import io.github.bumblesoftware.fastload.api.external.events.AbstractEvent;
import io.github.bumblesoftware.fastload.api.external.events.CapableEvent;
import io.github.bumblesoftware.fastload.api.internal.abstraction.Client1182;
import io.github.bumblesoftware.fastload.client.FLClientEvents;
import io.github.bumblesoftware.fastload.client.FLClientHandler;
import io.github.bumblesoftware.fastload.config.FLConfig;
import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.util.ObjectHolder;
import io.github.bumblesoftware.fastload.version.VersionConstants;
import net.fabricmc.api.ClientModInitializer;

import java.util.List;

import static io.github.bumblesoftware.fastload.config.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.config.FLMath.*;
import static io.github.bumblesoftware.fastload.init.Fastload.LOGGER;
import static io.github.bumblesoftware.fastload.version.VersionConstants.IS_MINECRAFT_1182;

public class FastloadClient implements ClientModInitializer {
    public static final AbstractEvent<ClientAbstractionContext> CLIENT_ABSTRACTION_EVENT;
    public static final AbstractionManager<AbstractClientCalls> MINECRAFT_ABSTRACTION;
    public static final AbstractionHandler<AbstractClientCalls> MINECRAFT_ABSTRACTION_HANDLER;

    static {
        VersionConstants.init();
        MINECRAFT_ABSTRACTION_HANDLER = new AbstractionHandler<>(
                List.of(""),
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
        CLIENT_ABSTRACTION_EVENT = new CapableEvent<>();
        registerBaseClient();
        MINECRAFT_ABSTRACTION = new AbstractionManagerImpl<>(getAbstractedClient());
        FLConfig.init();
        FLClientEvents.init();
        FLClientHandler.init();

        LOGGER.info("Fastload Perceived Version: " + MINECRAFT_ABSTRACTION.getVersion());
        LOGGER.info("Fastload Abstraction Supported Versions: " + MINECRAFT_ABSTRACTION.getSupportedMinecraftVersionsNonArray());
        LOGGER.info(logKey(DEBUG_KEY) + isDebugEnabled().toString().toUpperCase());
        LOGGER.info(logKey(CHUNK_TRY_LIMIT_KEY) + getChunkTryLimit());
        LOGGER.info(logKey(LOCAL_RENDER_RADIUS_KEY) + getLocalRenderChunkRadius());
        LOGGER.info(logKey(LOCAL_RENDER_AREA_KEY) + getLocalRenderChunkArea());
        LOGGER.info(logKey(SERVER_RENDER_RADIUS_KEY) + getServerRenderChunkRadius());
        LOGGER.info(logKey(SERVER_RENDER_AREA_KEY) + getServerRenderChunkArea());
    }

    @Override
    public void onInitializeClient() {}

    private static AbstractClientCalls getAbstractedClient() {
        if (CLIENT_ABSTRACTION_EVENT.isNotEmpty()) {
            var clientHolder = new ObjectHolder<AbstractClientCalls>(null);
            CLIENT_ABSTRACTION_EVENT.fire(new ClientAbstractionContext(clientHolder));
            if (clientHolder.heldObj != null)
                return clientHolder.heldObj;
        }
        throw new NullPointerException(
                "Method abstraction for MC Client is unsupported for this version. [VERSION: " + MINECRAFT_ABSTRACTION.getVersionUtil().providedVersion + "]"
        );
    }

    private static void registerBaseClient() {
        CLIENT_ABSTRACTION_EVENT.registerThreadUnsafe(0,
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (IS_MINECRAFT_1182) {
                        if (FLMath.isDebugEnabled())
                            Fastload.LOGGER.info("Fastload 1.18.2 Base!");
                        eventContext.clientCallsHolder.heldObj = new Client1182();
                    }
                })
        );
    }

    private static String logKey(String key) {
        return key.toUpperCase() + ": ";
    }

    public record ClientAbstractionContext(ObjectHolder<AbstractClientCalls> clientCallsHolder) {}
}
