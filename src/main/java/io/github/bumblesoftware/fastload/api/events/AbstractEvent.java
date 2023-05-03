package io.github.bumblesoftware.fastload.api.events;

import io.github.bumblesoftware.fastload.client.FLClientEvents;
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

    /**
     * Checks to see whether this event is empty and has no actions that are currently registered.
     * @return whether there are any active actions.
     */
    default boolean isNotEmpty() {
        for (final var location : getLocationList())
            if (isNotEmpty(location))
                return true;
        return false;
    }

    /**
     * Checks to see whether this event location is empty and has no actions that are currently registered.
     * @param locations a location (or subbranch) for this event to check.
     * @return whether there are any active actions in this location.
     */
    default boolean isNotEmpty(final String... locations) {
        for (final var location : locations) {
            final var eventHolder = getStorage().get(location);
            if (eventHolder != null) {
                for (final var priority : eventHolder.priorityHolder()) {
                    if (!eventHolder.argsHolder.get(priority.longValue()).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    /**
     * This is to get a list of locations for which the event will be called. Locations are essentially
     * nested events to create multiple different sub event streams for the same contexts. It's used to
     * save memory and clean up code a bit. Locations should not be used to distinguish between immutable
     * and mutable contexts.
     * @return a list of locations.
     */
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
     * Registers a lambda to a map. A thread safe alternative to {@link #registerThreadUnsafe(long, List, EventArgs)},
     * at the expense of speed & efficiency.
     * @param eventArgs The args that will be called when event fires.
     * @param locations All locations that this eventArg should be registered to.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    void registerThreadsafe(final long priority, final List<String> locations, final EventArgs<Context> eventArgs);

    /**
     * StableEventArgs alternative to {@link AbstractEvent#registerThreadsafe(long, List, EventArgs)}.
     * @param eventArgs The args that will be called when event fires.
     * @param locations All locations that this eventArg should be registered to.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    default void registerThreadsafe(
            final long priority,
            final List<String> locations,
            final StableEventArgs<Context> eventArgs
    ) {
        registerThreadsafe(priority, locations, eventArgs.upcast());
    }

    /**
     * Generic location alternative to {@link AbstractEvent#registerThreadsafe(long, List, EventArgs)}.
     * @param eventArgs The args that will be called when event fires.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    default void registerThreadsafe(final long priority, final EventArgs<Context> eventArgs) {
        registerThreadsafe(priority, GENERIC_LOCATION_LIST, eventArgs);
    }

    /**
     * StableEventArgs alternative to {@link AbstractEvent#registerThreadsafe(long, EventArgs)}.
     * @param eventArgs The args that will be called when event fires.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    default void registerThreadsafe(final long priority, final StableEventArgs<Context> eventArgs) {
        registerThreadsafe(priority, GENERIC_LOCATION_LIST, eventArgs.upcast());
    }

    /**
     * Registers a lambda to a map. It's not thread safe so do it at startup to avoid CME's.
     * @param eventArgs The args that will be called when event fires.
     * @param locations All locations that this eventArg should be registered to.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    void registerThreadUnsafe(final long priority, final List<String> locations, final EventArgs<Context> eventArgs);

    /**
     * StableEventArgs alternative to {@link AbstractEvent#registerThreadUnsafe(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event fires.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    @SuppressWarnings("unused")
    default void registerThreadUnsafe(final long priority, final List<String> locations, final StableEventArgs<Context> eventArgs) {
        registerThreadUnsafe(priority, locations, eventArgs.upcast());
    }

    /**
     * Generic location alternative to {@link AbstractEvent#registerThreadUnsafe(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event fires.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    default void registerThreadUnsafe(final long priority, final EventArgs<Context> eventArgs) {
        registerThreadUnsafe(priority, GENERIC_LOCATION_LIST, eventArgs);
    }

    /**
     * StableEventArgs alternative to {@link AbstractEvent#registerThreadUnsafe(long, EventArgs)}
     * @param eventArgs The args that will be called when event fires.
     * @param priority Holds the value for the priority value, which correlates with when it's fired.
     */
    default void registerThreadUnsafe(final long priority, final StableEventArgs<Context> eventArgs) {
        registerThreadUnsafe(priority, GENERIC_LOCATION_LIST, eventArgs.upcast());
    }

    /**
     * To set up an event listener, just mixin to your target, call this method with a new event context.
     * See {@link AbstractEvent} for what an actual implementation looks like.
     * @param locations All locations to be fired.
     * @param orderFlipped should event order be flipped when firing?
     * @param eventContext The provided context (of params) through this classes generic.
     */
    void fire(final List<String> locations, final boolean orderFlipped, final Context eventContext);

    /**
     * A default ordered alternative to {@link AbstractEvent#fire(List, boolean, Context)}
     * @param locations All locations to be fired.
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void fire(final List<String> locations, final Context eventContext) {
        fire(locations, false, eventContext);
    }

    /**
     * Generic location alternative to {@link AbstractEvent#fire(List, boolean, Context)}
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void fire(final boolean orderFlipped, final Context eventContext) {
        fire(GENERIC_LOCATION_LIST, orderFlipped, eventContext);
    }

    /**
     * Generic location and default ordered alternative to {@link AbstractEvent#fire(List, boolean, Context)}
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void fire(final Context eventContext) {
        fire(GENERIC_LOCATION_LIST, false, eventContext);
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
        @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
        EventArgs<Ctx> recursive(
                final Ctx eventContext,
                final AbstractEvent<Ctx> event,
                final Object closer,
                final EventArgs<Ctx> eventArgs
        );
    }

    @FunctionalInterface
    interface StableEventArgs<Ctx> extends EventArgs<Ctx> {

        default EventArgs<Ctx> upcast() {
            return this;
        }

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
            stable(eventContext, event, eventArgs);
            return null;
        }
        /**
         * @param eventContext The events params.
         * @param eventArgs The current instance provided for return (recursion)
         */
        void stable(
                final Ctx eventContext,
                final AbstractEvent<Ctx> event,
                final EventArgs<Ctx> eventArgs
        );
    }
}
