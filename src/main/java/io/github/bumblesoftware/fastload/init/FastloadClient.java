package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.abstraction.tool.AbstractClientCalls;
import io.github.bumblesoftware.fastload.client.FLClientEvents;
import io.github.bumblesoftware.fastload.client.FLClientHandler;
import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.github.bumblesoftware.fastload.util.MinecraftVersionUtil;
import net.fabricmc.api.ClientModInitializer;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;
import static io.github.bumblesoftware.fastload.init.Fastload.LOGGER;
import static io.github.bumblesoftware.fastload.init.FastloadHookable.getAbstractedClient;

public class FastloadClient implements ClientModInitializer {
    public static final AbstractClientCalls ABSTRACTED_CLIENT = getAbstractedClient();

    @Override
    public void onInitializeClient() {
        FLConfig.init();
        FLClientEvents.init();
        FLClientHandler.init();
        MinecraftVersionUtil.getVersion();
        LOGGER.info("Fastload Perceived Version: " + MinecraftVersionUtil.getVersion());
        LOGGER.info("Fastload Internal Mapping Version: " + ABSTRACTED_CLIENT.getVersion());
        LOGGER.info(logKey(DEBUG_KEY) + isDebugEnabled().toString().toUpperCase());
        LOGGER.info(logKey(TRY_LIMIT_KEY) + getChunkTryLimit());
        LOGGER.info(logKey(RENDER_RADIUS_KEY) + getRenderChunkRadius());
        LOGGER.info(logKey(RENDER_AREA_KEY) + getPreRenderArea());
    }

    private static String logKey(String key) {
        return key.toUpperCase() + ": ";
    }
}
