package io.github.bumblesoftware.fastload.api.events;

import java.util.ArrayList;

/**
 * Abstract event interface that provides the necessary tools for a generic event. Implement this in a class to create
 * your very own event. When returning a value to getEventHolder, make sure that it's a field that you're returning,
 * otherwise you'll be returning a new instance of nothing everytime the event is fired.
 */
@FunctionalInterface
public interface AbstractEvent<T extends Record> {
    /**
     * This abstract method requires a ArrayList Field in order to store all the  event registrations attached to
     * your given method.
     */
    ArrayList<EventArgs<T>> getEventHolder();

    /**
     *  Registers by adding the given EventArgs impl to the arraylist that stores all impl. This is the method
     *  that gets called to add your custom method body off the event.
     */
    default void register(EventArgs<T> eventContext) {
        getEventHolder().add(eventContext);
    }

    /**
     * Removes an event out of the storage. Use this when your events are only temporary
     */
    default void removeEvent(EventArgs<T> eventContext) {
        getEventHolder().remove(eventContext);
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
     * A functional interface that takes in a record as its type. This is to be able to store multiple objects and params into one
     * object holder so that custom interfaces do not need to be created.
     */
    @FunctionalInterface
    interface EventArgs<T> {
        void args(T eventContext);
    }
}