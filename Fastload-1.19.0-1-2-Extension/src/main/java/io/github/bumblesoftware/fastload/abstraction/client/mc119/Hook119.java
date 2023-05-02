package io.github.bumblesoftware.fastload.abstraction.client.mc119;

import io.github.bumblesoftware.fastload.api.external.abstraction.core.handler.AbstractionEntrypoint;
import io.github.bumblesoftware.fastload.api.external.abstraction.core.handler.MethodAbstractionApi;
import io.github.bumblesoftware.fastload.api.external.events.AbstractEvent;
import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.ObjectHolder;

import static io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning.VersionConstants.*;

public class Hook119 implements AbstractionEntrypoint {
    @Override
    public <A extends MethodAbstractionApi> void register(AbstractEvent<ObjectHolder<A>> clientAbstractionEvent) {
        clientAbstractionEvent.registerThreadUnsafe(1,
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (IS_MINECRAFT_1190 || IS_MINECRAFT_1191 || IS_MINECRAFT_1192) {
                        if (FLMath.isDebugEnabled())
                            Fastload.LOGGER.info("Fastload 1.19.0-1-2 Hook!");
                        eventContext.heldObj = new Client119().generify();
                    }
                })
        );
    }
}
