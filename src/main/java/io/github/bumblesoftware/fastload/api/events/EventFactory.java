package io.github.bumblesoftware.fastload.api.events;

import io.github.bumblesoftware.fastload.client.FLClientEvents;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


/**
 * EventFactory is a simple but powerful tool. It's easy to use, allows for recursion & dynamic registration.
 * Examples of how to register events are found in {@link io.github.bumblesoftware.fastload.client.FLClientHandler
 * FlClientHandler} & examples of instantiation are found in {@link FLClientEvents
 * FLEvents}. Since it's 100% abstract, it can be used anywhere, ranging from mixins and even from other events
 * including ones of this type. To use this, see javadocs for individual things inside this to see what they do.
 *
 * @param <T> used for custom event params. Refer to {@link FLClientEvents FLEvents}
 *           for examples.
 */
public class EventFactory<T extends Record> {

    /**
     * @return a new {@link EventHolder EventHolder}
     */
    public EventHolder<T> getNewHolder() {
        return new EventHolder<>(new HashMap<>(), new ArrayList<>());
    }

    /**
     * Stores all the args that are registered, along with their priorities.
     */
    public final EventHolder<T> argsHolder = getNewHolder();

    /**
     * Stores events that are queried to be removed after it has been fired.
     */
    public final EventHolder<T> eventsToRemove = getNewHolder();

    /**
     * This method isn't used by default, This is used if one wants to manage other events
     * @param identifier used to discriminate
     * @return multiple holders to manage dynamic events
     */
    @SuppressWarnings("unused")
    public EventHolder<T> getMultipleArgsHolders(String identifier) {
        return null;
    }

    /**
     * This is used to remove events during an eventArgs call. Use {@link #staticallyRemoveEvent(long, EventArgs)}
     * anywhere else.
     * @param eventArgs Args that are to be called upon the eventArgs getting fired.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    public void dynamicallyRemoveEvent(long priority, EventArgs<T> eventArgs) {
        eventsToRemove.priorityHolder.add(priority);
        eventsToRemove.argsHolder.get(priority).add(eventArgs);
    }

    /** Removes an eventArgs from the registry directly. Do not use this inside an eventArgs, or it will crash.
     * Instead, use {@link #dynamicallyRemoveEvent(long, EventArgs)}.
     * @param eventArgs Args that are to be called upon the eventArgs getting fired.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    public void staticallyRemoveEvent(long priority, EventArgs<T> eventArgs) {
        argsHolder.argsHolder.get(priority).remove(eventArgs);
    }

    /**
     * Adds the given event, and it's priority to the registry to be iterated through when the event fires.
     * @param eventArgs Args that are to be called upon the eventArgs getting fired.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    public void register(long priority, EventArgs<T> eventArgs) {
        if (!argsHolder.priorityHolder.contains(priority))
            argsHolder.priorityHolder.add(priority);

        argsHolder.argsHolder.putIfAbsent(priority, new ArrayList<>());
        argsHolder.argsHolder.get(priority).add(eventArgs);
    }

    /**
     * To fire an event, it must be called with a new record, in order to pass any amount of params without
     * requiring to change this method signature. Upon firing, the event will iterate through the registered
     * args in order of priority from highest to lowest. Then iterate through {@link #eventsToRemove} to remove certain events
     * dynamically. Finally, after that's done, {@link #eventsToRemove} will be emptied to save memory.
     * @param eventContext a new instance of a record with the type of this event.
     */
    public void fireEvent(T eventContext) {
        argsHolder.priorityHolder.sort(Comparator.reverseOrder());

        for (long priority : argsHolder.priorityHolder) {
            for (EventArgs<T> eventArgs : argsHolder.argsHolder.get(priority)) {
                if (eventArgs == null) return;
                eventArgs.recurse(eventContext, this, new Object(), eventArgs);
            }
        }

        for (long priority : eventsToRemove.priorityHolder)
            for (EventArgs<T> eventArgs : eventsToRemove.argsHolder.get(priority)) {
                if (eventArgs == null) return;
                argsHolder.argsHolder.get(priority).remove(eventArgs);
        }

        eventsToRemove.priorityHolder.clear();
        eventsToRemove.argsHolder.clear();
    }

    /**
     * Common storage type for {@link EventFactory AbstractEventFactory}
     * @param argsHolder Holds an array of {@link EventArgs} attached to a key of a given priority.
     * @param priorityHolder Holds all the priorities that are used to access a specific arraylist of events to iterate through.
     * @param <T> used for custom event params. Refer to {@link FLClientEvents FLEvents}
     *           for examples.
     */
    protected record EventHolder<T extends Record>(HashMap<Long, ArrayList<EventArgs<T>>> argsHolder, ArrayList<Long> priorityHolder) {}
}