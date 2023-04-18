package io.github.bumblesoftware.fastload.api.events;

import io.github.bumblesoftware.fastload.client.FLClientEvents;
import io.github.bumblesoftware.fastload.util.ObjectHolder;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic event system that's able to dynamically & statically registerBaseClient/remove,
 * has a builtin priority system & is easy to understand.
 * @param <Context> used for custom event params. Refer to {@link FLClientEvents}
 *           for examples.
 */
public interface AbstractEvent<Context> {
    String GENERIC_LOCATION = "generic";
    List<String> GENERIC_LOCATION_LIST = List.of(GENERIC_LOCATION);

    default EventArgs<Context> stableArgs(StableEventArgs<Context> eventArgs) {
        return eventArgs;
    }

    default boolean isNotEmpty() {
        final var value = new ObjectHolder<>(false);
        for (final var location : getLocationList()) {
            final var eventHolder = getStorage().get(location);
            if (eventHolder != null) {
                for (final var priority : eventHolder.priorityHolder()) {
                    if (!eventHolder.argsHolder.get(priority.longValue()).isEmpty()) {
                        value.heldObj = true;
                        break;
                    }
                }
            }
            if (value.heldObj)
                break;
        }
        return value.heldObj;
    }

    /**
     * Method to clean events in order to prevent memory leaks.
     */
    void clean();

    /**
     * @return a new {@link EventHolder EventHolder}
     */
    default EventHolder<Context> getNewHolder() {
        return new EventHolder<>(new Long2ObjectLinkedOpenHashMap<>(), new ArrayList<>());
    }

    /**
     * @return The current {@link EventHolder argsHolder} for usage in
     * {@link #removeThreadUnsafe(long, List, EventArgs)}
     */
    Object2ObjectMap<String, EventHolder<Context>> getStorage();

    List<String> getLocationList();

    /**
     * Thread safe method to remove events. Typically used inside of events to remove after fire.
     * @param eventArgs The args that will be called when event fires.
     * @param locations All locations that this eventArg should be removed from.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    void removeThreadSafe(
            final long priority,
            final List<String> locations,
            final EventArgs<Context> eventArgs
    );

    /**
     * Generic location alternative to {@link AbstractEvent#removeThreadSafe(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event fires.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    default void removeThreadSafe(final long priority, final EventArgs<Context> eventArgs) {
        removeThreadSafe(priority, GENERIC_LOCATION_LIST, eventArgs);
    }


    /**
     * Thread unsafe method of removing events. It's faster but will cause CME's if you aren't careful.
     * @param eventArgs The args that will be called when event fires.
     * @param locations All locations that this eventArg should be removed from.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    void removeThreadUnsafe(
            final long priority,
            final List<String> locations,
            final EventArgs<Context> eventArgs
    );

    /**
     * Generic location alternative to {@link AbstractEvent#removeThreadUnsafe(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event fires.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    default void removeThreadUnsafe(final long priority, final EventArgs<Context> eventArgs) {
           removeThreadUnsafe(priority, GENERIC_LOCATION_LIST, eventArgs);
    }

    /**
     * Registers a lambda to a map. A thread safe alternative to {@link #registerThreadUnsafe(long, List, ArgsProvider)},
     * at the expense of speed & efficiency.
     * @param argsProvider Args that are to be called upon the eventArgs getting fired.
     * @param locations All locations that this eventArg should be registered to.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    void registerThreadsafe(final long priority, final List<String> locations, final ArgsProvider<Context> argsProvider);

    /**
     * Generic location alternative to {@link AbstractEvent#registerThreadsafe(long, List, ArgsProvider)}
     * @param argsProvider Args that are to be called upon the eventArgs getting fired.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    default void registerThreadsafe(final long priority, final ArgsProvider<Context> argsProvider) {
        registerThreadsafe(priority, GENERIC_LOCATION_LIST, argsProvider);
    }

    /**
     * Registers a lambda to a map. It's not thread safe so do it at startup to avoid CME's.
     * @param argsProvider Args that are to be called upon the eventArgs getting fired.
     * @param locations All locations that this eventArg should be registered to.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    void registerThreadUnsafe(final long priority, final List<String> locations, final ArgsProvider<Context> argsProvider);

    /**
     * Generic location alternative to {@link AbstractEvent#registerThreadUnsafe(long, List, ArgsProvider)}
     * @param argsProvider Args that are to be called upon the eventArgs getting fired.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    default void registerThreadUnsafe(final long priority, final ArgsProvider<Context> argsProvider) {
        registerThreadUnsafe(priority, GENERIC_LOCATION_LIST, argsProvider);
    }

    /**
     * To set up an event listener, just mixin to your target, call this method with a new event context.
     * See {@link AbstractEvent} for what an actual implementation looks like.
     * @param locations All locations to be fired.
     * @param eventContext The provided context (of params) through this classes generic.
     */
    void fireEvent(final List<String> locations, final Context eventContext);

    /**
     * Generic location alternative to {@link AbstractEvent#fireEvent(List, Context)}
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void fireEvent(final Context eventContext) {
        fireEvent(GENERIC_LOCATION_LIST, eventContext);
    }

    /**
     * Common storage type for {@link AbstractEvent}
     * @param argsHolder Holds an array of {@link EventArgs} attached to a key of a given priority.
     * @param priorityHolder Holds all the priorities that are used to access a specific arraylist of events to onElement through.
     * @param <Ctx> used for custom event params. Refer to {@link FLClientEvents FLEvents}
     *           for examples.
     */
    record EventHolder<Ctx> (
            Long2ObjectMap<List<EventArgs<Ctx>>> argsHolder,
            ArrayList<Long> priorityHolder
    ) {}

    /**
     * Event args holds a specific implementation that is added to the main registry for {@link AbstractEvent}
     * to onElement through upon firing.
     * @param <Ctx> used for custom event params. Refer to {@link FLClientEvents FLEvents}
     *           for examples.
     */
    @FunctionalInterface
    interface EventArgs<Ctx> {

        /**
         * @param eventContext The events params.
         * @param event The event instance for managerial purposes.
         * @param closer The instance responsible for holding the value that will close this loop.
         * @param eventArgs The current instance provided for return (recursion)
         * @return The current instance.
         */
        @SuppressWarnings("UnusedReturnValue")
        EventArgs<Ctx> recursive(
                final Ctx eventContext,
                final AbstractEvent<Ctx> event,
                final Object closer,
                final EventArgs<Ctx> eventArgs
        );
    }

    @FunctionalInterface
    interface StableEventArgs<Ctx> extends EventArgs<Ctx> {

        /**
         * @param eventContext The events params.
         * @param event The event instance for managerial purposes.
         * @param closer The instance responsible for holding the value that will close this loop.
         * @param eventArgs The current instance provided for return (recursion)
         * @return The current instance.
         */
        @Override
        default EventArgs<Ctx> recursive(
                final Ctx eventContext,
                final AbstractEvent<Ctx> event,
                final Object closer,
                final EventArgs<Ctx> eventArgs
        ) {
            stable(eventContext, closer, eventArgs);
            return null;
        }
        /**
         * @param eventContext The events params.
         * @param closer The instance responsible for holding the value that will close this loop.
         * @param eventArgs The current instance provided for return (recursion)
         */
        void stable(
                final Ctx eventContext,
                final Object closer,
                final EventArgs<Ctx> eventArgs
        );
    }

    @FunctionalInterface
    interface ArgsProvider<Ctx> {
        EventArgs<Ctx> getEvent(final AbstractEvent<Ctx> event);
    }
}
