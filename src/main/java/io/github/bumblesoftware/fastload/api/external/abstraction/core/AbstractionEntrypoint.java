package io.github.bumblesoftware.fastload.api.external.abstraction.core;

import io.github.bumblesoftware.fastload.api.external.events.AbstractEvent;
import io.github.bumblesoftware.fastload.util.ObjectHolder;

public interface AbstractionEntrypoint {
    <A extends AbstractionApi> void register(AbstractEvent<ObjectHolder<A>> clientAbstractionEvent);
}
