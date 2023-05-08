package io.github.bumblesoftware.fastload.api.abstraction.core.handler;

import io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionUtil;
import io.github.bumblesoftware.fastload.api.event.core.AbstractEvent;
import io.github.bumblesoftware.fastload.util.obj_holders.MutableObjectHolder;

public interface AbstractionEntrypoint {
    <A extends MethodAbstractionApi> void registerAbstraction(AbstractEvent<MutableObjectHolder<A>> clientAbstractionEvent);
    default void loadClasses(@SuppressWarnings("unused") AbstractEvent<VersionUtil> classLoadEvent) {}
}
