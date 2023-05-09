package io.github.bumblesoftware.fastload.abstraction.client.mc1194;

import io.github.bumblesoftware.fastload.api.abstraction.core.handler.AbstractionEntrypoint;
import io.github.bumblesoftware.fastload.api.abstraction.core.handler.MethodAbstractionApi;
import io.github.bumblesoftware.fastload.api.event.core.AbstractEvent;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.obj_holders.MutableObjectHolder;

import static io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionConstants.IS_MINECRAFT_1194;
import static io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionConstants.IS_MINECRAFT_1200;
import static io.github.bumblesoftware.fastload.config.FLMath.ifDebugEnabled;

public class Hook1194 implements AbstractionEntrypoint {
    @Override
    public <A extends MethodAbstractionApi> void registerAbstraction(AbstractEvent<MutableObjectHolder<A>> clientAbstractionEvent) {
        clientAbstractionEvent.registerStatic(3,
                (eventContext, eventStatus, event, eventArgs) -> {
                    if (IS_MINECRAFT_1194 || IS_MINECRAFT_1200) {
                        ifDebugEnabled(() ->
                            Fastload.LOGGER.info("Fastload 1.19.4 Hook!"));
                        eventContext.setHeldObj(new Client1194().generify());
                    }
                }
        );
    }
}
