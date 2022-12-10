package io.github.bumblesoftware.fastload.api.events.abstract_events;

import java.util.ArrayList;

/**
 * Abstract event that holds all the necessary tools to run an event
 */
public interface AbstractEvent<T extends Record> {
    ArrayList<EventArgs<T>> getEventHolder();
    default void register(EventArgs<T> event) {
        getEventHolder().add(event);
    }
    default void fireEvent(T eventContext) {
        for (EventArgs<T> eventCaller : getEventHolder()) {
            eventCaller.args(eventContext);
        }
    }
    @FunctionalInterface
    interface EventArgs<T> {
        void args(T eventContext);
    }
}