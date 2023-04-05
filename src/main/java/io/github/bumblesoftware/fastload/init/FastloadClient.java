package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.abstraction.tool.AbstractClientCalls;
import io.github.bumblesoftware.fastload.abstraction.client.Client1182;
import io.github.bumblesoftware.fastload.client.FLClientEvents;
import io.github.bumblesoftware.fastload.client.FLClientHandler;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.util.MinecraftVersionUtil;
import net.fabricmc.api.ClientModInitializer;

public class FastloadClient implements ClientModInitializer {
    public static final AbstractClientCalls ABSTRACTED_CLIENT = getAbstractedClient();

    private static AbstractClientCalls getAbstractedClient() {
        if (FLMath.isDebugEnabled())
            Fastload.LOGGER.info("fastload1182base");
        if (MinecraftVersionUtil.matchesAny("1.18.2"))
            return new Client1182();
        else throw new NullPointerException("Method abstraction for MC Client is unsupported for this version");
    }

    @Override
    public void onInitializeClient() {
        FLClientEvents.init();
        FLClientHandler.init();
        MinecraftVersionUtil.getVersion();
        if (FLMath.isDebugEnabled())
            Fastload.LOGGER.info("Fastload Internal Mapping Version: " + ABSTRACTED_CLIENT.getVersion());
    }
}
