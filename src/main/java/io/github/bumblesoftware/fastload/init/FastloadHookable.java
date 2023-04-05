package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.abstraction.client.Client1182;
import io.github.bumblesoftware.fastload.abstraction.tool.AbstractClientCalls;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.util.MinecraftVersionUtil;

public class FastloadHookable {
    static AbstractClientCalls getAbstractedClient() {
        if (FLMath.isDebugEnabled())
            Fastload.LOGGER.info("Fastload 1.18.2 Base!");
        if (MinecraftVersionUtil.matchesAny("1.18.2"))
            return new Client1182();
        else throw new NullPointerException("Method abstraction for MC Client is unsupported for this version");
    }
}
