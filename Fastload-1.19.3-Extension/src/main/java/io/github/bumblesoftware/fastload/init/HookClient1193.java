package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.abstraction.client.Client1193;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import net.fabricmc.api.ClientModInitializer;

import static io.github.bumblesoftware.fastload.abstraction.tool.AbstractionEvents.CLIENT_ABSTRACTION_EVENT;
import static io.github.bumblesoftware.fastload.util.MinecraftVersionUtil.matchesAny;

public class HookClient1193 implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CLIENT_ABSTRACTION_EVENT.registerThreadUnsafe(2, (eventContext, event, closer, eventArgs) -> {
            if (matchesAny("1.19.3")) {
                if (FLMath.isDebugEnabled())
                    Fastload.LOGGER.info("Fastload 1.19.3 Hook!");
                eventContext.clientCalls = new Client1193();
            }
            return null;
        });
    }
}
