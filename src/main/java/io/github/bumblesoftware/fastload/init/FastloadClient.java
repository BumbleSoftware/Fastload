package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.abstraction.client.AbstractClientCalls;
import io.github.bumblesoftware.fastload.abstraction.client.Client1182;
import io.github.bumblesoftware.fastload.api.events.AbstractEvent;
import io.github.bumblesoftware.fastload.api.events.CapableEvent;
import io.github.bumblesoftware.fastload.client.FLClientEvents;
import io.github.bumblesoftware.fastload.client.FLClientHandler;
import io.github.bumblesoftware.fastload.config.FLConfig;
import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.util.MinecraftVersionUtil;
import io.github.bumblesoftware.fastload.util.ObjectHolder;
import net.fabricmc.api.ClientModInitializer;

import static io.github.bumblesoftware.fastload.config.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.config.FLMath.*;
import static io.github.bumblesoftware.fastload.init.Fastload.LOGGER;
import static io.github.bumblesoftware.fastload.util.MinecraftVersionUtil.EQUALS;
import static io.github.bumblesoftware.fastload.util.MinecraftVersionUtil.getVersion;

public class FastloadClient implements ClientModInitializer {
    public static final AbstractClientCalls ABSTRACTED_CLIENT;
    public static final AbstractEvent<ClientAbstractionContext> CLIENT_ABSTRACTION_EVENT;

    static {
        CLIENT_ABSTRACTION_EVENT = new CapableEvent<>();
        registerBaseClient();
        ABSTRACTED_CLIENT = getAbstractedClient();
        FLConfig.init();
        FLClientEvents.init();
        FLClientHandler.init();

        LOGGER.info("Fastload Perceived Version: " + getVersion());
        LOGGER.info("Fastload Abstraction Supported Versions: " + ABSTRACTED_CLIENT.getCompatibleVersions());
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
            CLIENT_ABSTRACTION_EVENT.fireEvent(new ClientAbstractionContext(clientHolder));
            if (clientHolder.heldObj != null)
                return clientHolder.heldObj;
        }
        throw new NullPointerException(
                "Method abstraction for MC Client is unsupported for this version. [VERSION: " + getVersion() +"]");
    }

    private static void registerBaseClient() {
        CLIENT_ABSTRACTION_EVENT.registerThreadUnsafe(0,
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (MinecraftVersionUtil.matchesAny(EQUALS, "1.18.2")) {
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
