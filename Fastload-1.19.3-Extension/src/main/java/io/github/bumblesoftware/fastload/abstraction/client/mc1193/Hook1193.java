package io.github.bumblesoftware.fastload.abstraction.client.mc1193;

import io.github.bumblesoftware.fastload.api.external.abstraction.core.handler.AbstractionEntrypoint;
import io.github.bumblesoftware.fastload.api.external.abstraction.core.handler.MethodAbstractionApi;
import io.github.bumblesoftware.fastload.api.external.events.AbstractEvent;
import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.ObjectHolder;

import static io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning.VersionConstants.*;

public class Hook1193 implements AbstractionEntrypoint {
    @Override
    public <A extends MethodAbstractionApi> void register(AbstractEvent<ObjectHolder<A>> clientAbstractionEvent) {
        clientAbstractionEvent.registerThreadUnsafe(2,
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (IS_MINECRAFT_1193) {
                        if (FLMath.isDebugEnabled())
                            Fastload.LOGGER.info("Fastload 1.19.3 Hook!");
                        eventContext.heldObj = new Client1193().generify();
                    }
                })
        );
    }
}
