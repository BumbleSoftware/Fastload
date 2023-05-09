package io.github.bumblesoftware.fastload.abstraction.client.mc119;

import io.github.bumblesoftware.fastload.api.abstraction.core.handler.AbstractionEntrypoint;
import io.github.bumblesoftware.fastload.api.abstraction.core.handler.MethodAbstractionApi;
import io.github.bumblesoftware.fastload.api.event.core.AbstractEvent;
import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.obj_holders.MutableObjectHolder;

import static io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionConstants.*;

public class Hook119 implements AbstractionEntrypoint {
    @Override
    public <A extends MethodAbstractionApi> void registerAbstraction(AbstractEvent<MutableObjectHolder<A>> clientAbstractionEvent) {
        clientAbstractionEvent.registerStatic(1,
                (eventContext, eventStatus, event, eventArgs) -> {
                    if (IS_MINECRAFT_1190 || IS_MINECRAFT_1191 || IS_MINECRAFT_1192) {
                        FLMath.ifDebugEnabled(() ->
                            Fastload.LOGGER.info("Fastload 1.19.0-1-2 Hook!")
                        );
                        eventContext.setHeldObj(new Client119().generify());
                    }
                }
        );
    }
}
