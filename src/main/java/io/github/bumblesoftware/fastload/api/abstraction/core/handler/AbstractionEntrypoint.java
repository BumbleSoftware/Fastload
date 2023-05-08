package io.github.bumblesoftware.fastload.api.abstraction.core.handler;

import io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionUtil;
import io.github.bumblesoftware.fastload.api.events.AbstractEvent;
import io.github.bumblesoftware.fastload.util.MutableObjectHolder;

public interface AbstractionEntrypoint {
    <A extends MethodAbstractionApi> void registerAbstraction(AbstractEvent<MutableObjectHolder<A>> clientAbstractionEvent);

    default void loadClasses(AbstractEvent<VersionUtil> classLoadEvent) {}
}
