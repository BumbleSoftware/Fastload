package io.github.bumblesoftware.fastload.api.external.abstraction.core.handler;

import io.github.bumblesoftware.fastload.api.external.events.AbstractEvent;
import io.github.bumblesoftware.fastload.util.MutableObjectHolder;

public interface AbstractionEntrypoint {
    <A extends MethodAbstractionApi> void register(AbstractEvent<MutableObjectHolder<A>> clientAbstractionEvent);
}
