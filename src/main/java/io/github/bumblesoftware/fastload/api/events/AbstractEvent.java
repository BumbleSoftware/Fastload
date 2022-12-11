package io.github.bumblesoftware.fastload.api.events;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Abstract event interface that provides the necessary tools for a generic event. Implement this in a class to create
 * your very own event. Or alternatively, instantiate this interface. Be sure with whichever that you chose, to return a field
 * and not a new instance. Otherwise, registries of this event won't be stored.
 */
@FunctionalInterface
public interface AbstractEvent<T extends Record> {
    /**
     * This abstract method requires a ArrayList Field in order to store all the  event registrations attached to
     * your given method.
     */
    EventHolder<T> getEventHolder();



    /**
     * Returns a new event holder
     */
    default EventHolder<T> newHolder() {
        return new EventHolder<>(new HashMap<>(), new ArrayList<>());
    }

    /**
     *  Registers by adding the given EventArgs impl to the arraylist that stores all impl. This is the method
     *  that gets called to add your custom method body off the event.
     */
    default void register(EventArgs<T> eventContext, long priority) {
        getEventHolder().argsHolder.putIfAbsent(priority, new ArrayList<>());
        getEventHolder().argsHolder.get(priority).add(eventContext);

        if (!getEventHolder().priorityHolder.contains(priority))
            getEventHolder().priorityHolder.add(priority);
    }

    default void registerRecursive(EventRecursiveArgs<T> eventContext, long priority) {
        register(eventContext, priority);
    }


    /**
     * Removes an event out of the storage. Use this when your events are only temporary
     */
    @SuppressWarnings("unused")
    default void removeEvent(EventArgs<T> eventContext, long priority) {
        getEventHolder().argsHolder.get(priority).remove(eventContext);
        getEventHolder().priorityHolder.remove(priority);
    }

    /**
     * In order to fire this event, you first need to instantiate it in a field. Then using that instance, you have to
     * call this method in your mixin's method of choice, with the given params. And that's it! Now whenever that method gets called,
     * so does every registered implementation in the given instance.
     */
    private void fireEvent(T eventContext, AbstractEvent<T> abstractEvent) {
        getEventHolder().priorityHolder.sort(Comparator.reverseOrder());
        for (Long priority : getEventHolder().priorityHolder) {
            for (EventArgs<T> eventArgs : getEventHolder().argsHolder.get(priority)) {
                if (eventArgs == null) {
                    return;
                }
                if (eventArgs instanceof AbstractEvent.EventRecursiveArgs<T> eventRecursion) {
                    eventRecursion.recurse(null, null, new Object(), eventRecursion);
                }
                eventArgs.args(eventContext, abstractEvent, eventArgs);
            }
        }
    }

    /**
     * Fires the event as normal but automatically passes the instance of which it is called in. In order to avoid
     * having to statically provide the instance.
     */
    default void fireEvent(T eventContext) {
        fireEvent(eventContext, this);
    }

    /**
     * A functional interface that takes in a record as its type. This is to be able to store multiple objects and params into one
     * object argsHolder so that custom interfaces do not need to be created.
     */
    @FunctionalInterface
    interface EventArgs<T extends Record> {
        /**
         * Provides event params. Supplies the instance of EventArgs in order to allow for complex recursion. Furthermore,
         * AbstractEvent is supplied to dynamically register and remove events with ease.
         */
        default EventArgs<T> recurse(
                @Nullable T eventContext,
                @Nullable AbstractEvent<T> abstractParent,
                @Nullable Object closer,
                @NotNull EventArgs<T> eventArgs)
        {
            if (closer == null) {
                return null;
            }
            eventArgs.args(eventContext, abstractParent, eventArgs);
            return eventArgs.recurse(eventContext, abstractParent, closer, eventArgs);
        }

        /**
         * Use this when you don't intend on using recursion
         */
        void args(T eventContext, AbstractEvent<T> abstractEvent , EventArgs<T> eventArgs);
    }

    /**
     * EventArgs but the functional method is the recursive alternative.
     */
    @FunctionalInterface
    interface EventRecursiveArgs<T extends Record> extends EventArgs<T> {
        @Override
        default void args(T eventContext, AbstractEvent<T> abstractEvent, EventArgs<T> eventArgs) {}

        /**
         * Abstract recursive method. Use with caution
         */
        @Override
        EventArgs<T> recurse(
                @Nullable T eventContext,
                @Nullable AbstractEvent<T> abstractParent,
                @Nullable Object closer,
                @NotNull EventArgs<T> eventArgs
        );
    }


    /**
     * Holds the necessary params in order to appropriately manage EventArgs and their priorities.
     */
    record EventHolder<T extends Record>(HashMap<Long, ArrayList<EventArgs<T>>> argsHolder, ArrayList<Long> priorityHolder) {}
}