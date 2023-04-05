package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.abstraction.tool.AbstractClientCalls;
import io.github.bumblesoftware.fastload.client.FLClientEvents;
import io.github.bumblesoftware.fastload.client.FLClientHandler;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.util.MinecraftVersionUtil;
import net.fabricmc.api.ClientModInitializer;

import static io.github.bumblesoftware.fastload.init.FastloadHookable.getAbstractedClient;

public class FastloadClient implements ClientModInitializer {
    public static final AbstractClientCalls ABSTRACTED_CLIENT = getAbstractedClient();

    @Override
    public void onInitializeClient() {
        FLClientEvents.init();
        FLClientHandler.init();
        MinecraftVersionUtil.getVersion();
        if (FLMath.isDebugEnabled())
            Fastload.LOGGER.info("Fastload Internal Mapping Version: " + ABSTRACTED_CLIENT.getVersion());
    }
}
