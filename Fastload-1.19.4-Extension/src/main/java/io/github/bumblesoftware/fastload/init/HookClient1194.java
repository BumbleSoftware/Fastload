package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.abstraction.client.Client1194;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import net.fabricmc.api.ClientModInitializer;

import static io.github.bumblesoftware.fastload.abstraction.tool.AbstractionEvents.CLIENT_ABSTRACTION_EVENT;
import static io.github.bumblesoftware.fastload.util.MinecraftVersionUtil.matchesAny;

public class HookClient1194 implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CLIENT_ABSTRACTION_EVENT.registerThreadUnsafe(3, (eventContext, event, closer, eventArgs) -> {
            if (matchesAny("1.19.4")) {
                if (FLMath.isDebugEnabled())
                    Fastload.LOGGER.info("Fastload 1.19.4 Hook!");
                eventContext.clientCalls = new Client1194();
            }
            return null;
        });
    }
}
