package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.abstraction.client.Client1182;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.util.MinecraftVersionUtil;

import static io.github.bumblesoftware.fastload.abstraction.tool.AbstractionEvents.CLIENT_ABSTRACTION_EVENT;

public class BuiltinAbstractMappings {
    public static void register() {
        CLIENT_ABSTRACTION_EVENT.registerThreadUnsafe(0, (eventContext, event, closer, eventArgs) -> {
            if (MinecraftVersionUtil.matchesAny("1.18.2")) {
                if (FLMath.isDebugEnabled())
                    Fastload.LOGGER.info("Fastload 1.18.2 Base!");
                eventContext.clientCalls = new Client1182();
            }
            return null;
        });

    }
}
