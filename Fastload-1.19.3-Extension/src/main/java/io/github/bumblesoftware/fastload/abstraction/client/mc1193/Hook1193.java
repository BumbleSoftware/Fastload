package io.github.bumblesoftware.fastload.abstraction.client.mc1193;

import io.github.bumblesoftware.fastload.api.abstraction.core.handler.AbstractionEntrypoint;
import io.github.bumblesoftware.fastload.api.abstraction.core.handler.MethodAbstractionApi;
import io.github.bumblesoftware.fastload.api.event.core.AbstractEvent;
import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.obj_holders.MutableObjectHolder;

import static io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionConstants.IS_MINECRAFT_1193;

public class Hook1193 implements AbstractionEntrypoint {

    @Override
    public <A extends MethodAbstractionApi> void registerAbstraction(AbstractEvent<MutableObjectHolder<A>> clientAbstractionEvent) {
        clientAbstractionEvent.registerStatic(2,
                (eventContext, eventStatus, event, eventArgs) -> {
                    if (IS_MINECRAFT_1193) {
                        FLMath.isDebugEnabled().runIf(() ->
                                Fastload.LOGGER.info("Fastload 1.19.3 Hook!")
                        );
                        eventContext.setHeldObj(new Client1193().generify());
                    }
                }
        );
    }
}
