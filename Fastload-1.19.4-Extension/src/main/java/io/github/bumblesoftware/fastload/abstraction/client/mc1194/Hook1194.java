package io.github.bumblesoftware.fastload.abstraction.client.mc1194;

import io.github.bumblesoftware.fastload.api.external.abstraction.core.handler.AbstractionEntrypoint;
import io.github.bumblesoftware.fastload.api.external.abstraction.core.handler.MethodAbstractionApi;
import io.github.bumblesoftware.fastload.api.external.events.AbstractEvent;
import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.ObjectHolder;

import static io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning.VersionConstants.IS_MINECRAFT_1194;
import static io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning.VersionConstants.IS_MINECRAFT_1200;

public class Hook1194 implements AbstractionEntrypoint {
    @Override
    public <A extends MethodAbstractionApi> void register(AbstractEvent<ObjectHolder<A>> clientAbstractionEvent) {
        clientAbstractionEvent.registerThreadUnsafe(3,
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (IS_MINECRAFT_1194 || IS_MINECRAFT_1200) {
                        if (FLMath.isDebugEnabled())
                            Fastload.LOGGER.info("Fastload 1.19.4 Hook!");
                        //noinspection unchecked
                        eventContext.heldObj = (A) new Client1194();
                    }
                })
        );
    }
}
