package io.github.bumblesoftware.fastload.api.events;

import io.github.bumblesoftware.fastload.client.FLClientEvents;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic event system that's able to dynamically & statically register/remove,
 * has a builtin priority system & is easy to understand.
 * @param <T> used for custom event params. Refer to {@link FLClientEvents}
 *           for examples.
 */
public interface AbstractEvent<T extends Record, T2> {

    /**
     * @return a new {@link EventHolder EventHolder}
     */
    default EventHolder<T, T2> getNewHolder() {
        return new EventHolder<>(new Long2ObjectLinkedOpenHashMap<>(), new ArrayList<>());
    }

    /**
     * Used if one wants an event that can multitask.
     * @param identifier used to discriminate
     * @return multiple holders to manage dynamic events
     */
    @SuppressWarnings("unused")
    EventHolder<T, T2> getMultipleArgsHolders(final T2 identifier);

    /**
     * @return The current {@link EventHolder argsHolder} for usage in {@link #removeThreadUnsafe(long, EventArgs)}
     */
    EventHolder<T, T2> getHolder();

    /**
     * Thread safe method to remove events. Typically used inside of events to remove after fire.
     * @param eventArgs Args that are to be called upon the eventArgs getting fired.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    void removeThreadSafe(final long priority, final EventArgs<T, T2> eventArgs);

    /**
     * Thread unsafe method of removing events. It's faster but will cause CME's if you aren't careful.
     * @param eventArgs Args that are to be called upon the eventArgs getting fired.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    default void removeThreadUnsafe(final long priority, final EventArgs<T, T2> eventArgs) {
            getHolder().argsHolder().get(priority).remove(eventArgs);
    }

    /**
     * Registers a lambda to a map. A thread safe alternative to {@link #registerThreadUnsafe(long, EventArgs)},
     * at the expense of speed & efficiency.
     * @param eventArgs Args that are to be called upon the eventArgs getting fired.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    void registerThreadsafe(final long priority, final EventArgs<T, T2> eventArgs);

    /**
     * Registers a lambda to a map. It's not thread safe so do it at startup to avoid CME's.
     * @param eventArgs Args that are to be called upon the eventArgs getting fired.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    void registerThreadUnsafe(final long priority, final EventArgs<T, T2> eventArgs);

    /**
     * To set up an event listener, just mixin to your target, call this method with a new event context.
     * See {@link AbstractEvent} for what an actual implementation looks like.
     * @param eventContext The provided context (of params) through this classes generic.
     */
    void fireEvent(final T eventContext);

    /**
     * Common storage type for {@link AbstractEvent}
     * @param argsHolder Holds an array of {@link EventArgs} attached to a key of a given priority.
     * @param priorityHolder Holds all the priorities that are used to access a specific arraylist of events to onElement through.
     * @param <T> used for custom event params. Refer to {@link FLClientEvents FLEvents}
     *           for examples.
     */
    record EventHolder<T extends Record, T2>(
            Long2ObjectMap<List<EventArgs<T, T2>>> argsHolder,
            ArrayList<Long> priorityHolder
    ) {}

    /**
     * Event args holds a specific implementation that is added to the main registry for {@link AbstractEvent}
     * to onElement through upon firing.
     * @param <T> used for custom event params. Refer to {@link FLClientEvents FLEvents}
     *           for examples.
     */
    @FunctionalInterface
    interface EventArgs<T extends Record, T2> {

        /**
         * @param eventContext The events params.
         * @param event The event instance for managerial purposes.
         * @param closer The instance responsible for holding the value that will close this loop.
         * @param eventArgs The current instance provided for return (recursion)
         * @return The current instance.
         */
        @SuppressWarnings("UnusedReturnValue")
        EventArgs<T, T2> onEvent(
                final T eventContext,
                final AbstractEvent<T, T2> event,
                final Object closer,
                final EventArgs<T, T2> eventArgs
        );
    }
}
