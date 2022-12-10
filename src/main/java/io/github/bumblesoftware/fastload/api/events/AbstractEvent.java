package io.github.bumblesoftware.fastload.api.events;

import java.util.ArrayList;

/**
 * Abstract event interface that provides the necessary tools for a generic event. Implement this in a class to create
 * your very own event. When returning a value to getEventHolder, make sure that it's a field that you're returning,
 * otherwise you'll be returning a new instance of nothing everytime the event is fired.
 */
public interface AbstractEvent<T extends Record> {
    /**
     * This abstract method requires a ArrayList field in order to store all the registered event registrations attached to
     * your given method.
     */
    ArrayList<EventArgs<T>> getEventHolder();
    default void register(EventArgs<T> eventContext) {
        getEventHolder().add(eventContext);
    }

    /**
     * In order to fire this event, you first need to instantiate it in a field. Then using that instance, you have to
     * call this method in your mixin's method of choice, with the given params. And that's it! Now whenever that method gets called,
     * so does every registered implementation in the given instance.
     */
    default void fireEvent(T eventContext) {
        for (EventArgs<T> eventCaller : getEventHolder()) {
            eventCaller.args(eventContext);
        }
    }

    /**
     * A functional interface that takes in the generic type as its param. This is why records are used. They are good for
     * packing and unpacking lots of objects without having to make more params.
     */
    @FunctionalInterface
    interface EventArgs<T> {
        void args(T eventContext);
    }
}