package io.github.bumblesoftware.fastload.updated_code.event.def;

import event.core.AbstractEvent;
import event.core.EventArgs;
import event.core.EventRegistry;

/**
 * EmptyEvent is used to debug events by essentially turning them off by assigning them this class.
 */
public class EmptyEvent<T> implements AbstractEvent<T> {

    private final EventRegistry<T> storage = new EventRegistry<>();

    @Override
    public void clean() {}

    @Override
    public EventRegistry<T> getRegistry() {
        return storage;
    }
    @Override
    public void removeListener(long priority, String location, EventArgs<T> eventArgs) {}

    @Override
    public void registerListener(long priority, String location, EventArgs<T> argsProvider) {}

    @Override
    public void execute(String locations, T eventContext) {}
}
