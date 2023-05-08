package io.github.bumblesoftware.fastload.abstraction.client.mc1193;

import io.github.bumblesoftware.fastload.api.abstraction.core.handler.AbstractionEntrypoint;
import io.github.bumblesoftware.fastload.api.abstraction.core.handler.MethodAbstractionApi;
import io.github.bumblesoftware.fastload.api.events.AbstractEvent;
import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.MutableObjectHolder;

import static io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionConstants.IS_MINECRAFT_1193;

public class Hook1193 implements AbstractionEntrypoint {
    @Override
    public <A extends MethodAbstractionApi> void registerAbstraction(AbstractEvent<MutableObjectHolder<A>> clientAbstractionEvent) {
        clientAbstractionEvent.registerThreadUnsafe(2,
                (eventContext,event, eventArgs) -> {
                    if (IS_MINECRAFT_1193) {
                        FLMath.isDebugEnabled().runIf(() ->
                            Fastload.LOGGER.info("Fastload 1.19.3 Hook!")
                        );
                        eventContext.heldObj = new Client1193().generify();
                    }
                }
        );
    }
}
