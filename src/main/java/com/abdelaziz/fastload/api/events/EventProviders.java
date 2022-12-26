package com.abdelaziz.fastload.api.events;

import com.abdelaziz.fastload.api.events.EventFactory.EventHolder;

public final class EventProviders {
    private EventProviders() {
    }

    public interface MultipleArgsHolders<T extends Record> {
        EventHolder<T> getMultipleArgsHolders(String identifier);
    }

    public interface DynamicallyRemoveEvent<T extends Record> {
        void dynamicallyRemoveEvent(long priority, EventArgs<T> eventArgs);
    }

    public interface StaticallyRemoveEvent<T extends Record> {
        void staticallyRemoveEvent(long priority, EventArgs<T> eventArgs);
    }

    public interface RegisterEvent<T extends Record> {
        void register(long priority, EventArgs<T> eventArgs);
    }

    public interface FireEvent<T extends Record> {
        void fireEvent(T eventContext);
    }
}
