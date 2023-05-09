package io.github.bumblesoftware.fastload.api.event.core;

import io.github.bumblesoftware.fastload.client.FLClientEvents;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
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
                    if (!eventHolder.argsHolder().get(priority.longValue()).isEmpty()) {
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
     * {@link #removeStatic(long, List, EventArgs)}
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
     * Thread safe method to remove events. Typically used inside of events to remove after execute.
     * @param eventArgs The args that will be called when event executes.
     * @param locations All locations that this eventArg should be removed from.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    @SuppressWarnings("unused")
    void removeDynamic(
            final long priority,
            final List<String> locations,
            final EventArgs<Context> eventArgs
    );

    /**
     * Generic location alternative to {@link AbstractEvent#removeDynamic(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    @SuppressWarnings("unused")
    default void removeDynamic(final long priority, final EventArgs<Context> eventArgs) {
        removeDynamic(priority, GENERIC_LOCATION_LIST, eventArgs);
    }

    /**
     * Thread unsafe method of removing events. It's faster but will cause CME's if you aren't careful.
     * @param eventArgs The args that will be called when event executes.
     * @param locations All locations that this eventArg should be removed from.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    @SuppressWarnings("unused")
    void removeStatic(
            final long priority,
            final List<String> locations,
            final EventArgs<Context> eventArgs
    );

    /**
     * Generic location alternative to {@link AbstractEvent#removeStatic(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event executed.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void removeStatic(final long priority, final EventArgs<Context> eventArgs) {
           removeStatic(priority, GENERIC_LOCATION_LIST, eventArgs);
    }

    /**
     * Registers a lambda to a map. A thread safe alternative to {@link #registerStatic(long, List, EventArgs)},
     * at the expense of speed & efficiency.
     * @param eventArgs The args that will be called when event executes.
     * @param locations All locations that this eventArg should be registered to.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    @SuppressWarnings("unused")
    void registerDynamic(final long priority, final List<String> locations, final EventArgs<Context> eventArgs);

    /**
     * StableEventArgs alternative to {@link AbstractEvent#registerDynamic(long, List, EventArgs)}.
     * @param eventArgs The args that will be called when event executes.
     * @param locations All locations that this eventArg should be registered to.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    @SuppressWarnings("unused")
    default void registerDynamic(
            final long priority,
            final List<String> locations,
            final StableEventArgs<Context> eventArgs
    ) {
        registerDynamic(priority, locations, eventArgs.upcast());
    }

    /**
     * Generic location alternative to {@link AbstractEvent#registerDynamic(long, List, EventArgs)}.
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    @SuppressWarnings("unused")
    default void registerDynamic(final long priority, final EventArgs<Context> eventArgs) {
        registerDynamic(priority, GENERIC_LOCATION_LIST, eventArgs);
    }

    /**
     * StableEventArgs alternative to {@link AbstractEvent#registerDynamic(long, EventArgs)}.
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    @SuppressWarnings("unused")
    default void registerDynamic(final long priority, final StableEventArgs<Context> eventArgs) {
        registerDynamic(priority, GENERIC_LOCATION_LIST, eventArgs.upcast());
    }

    /**
     * Registers a lambda to a map. It's not thread safe so do it at startup to avoid CME's.
     * @param eventArgs The args that will be called when event executes.
     * @param locations All locations that this eventArg should be registered to.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    void registerStatic(final long priority, final List<String> locations, final EventArgs<Context> eventArgs);

    /**
     * StableEventArgs alternative to {@link AbstractEvent#registerStatic(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    @SuppressWarnings("unused")
    default void registerStatic(final long priority, final List<String> locations, final StableEventArgs<Context> eventArgs) {
        registerStatic(priority, locations, eventArgs.upcast());
    }

    /**
     * Generic location alternative to {@link AbstractEvent#registerStatic(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerStatic(final long priority, final EventArgs<Context> eventArgs) {
        registerStatic(priority, GENERIC_LOCATION_LIST, eventArgs);
    }

    /**
     * StableEventArgs alternative to {@link AbstractEvent#registerStatic(long, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerStatic(final long priority, final StableEventArgs<Context> eventArgs) {
        registerStatic(priority, GENERIC_LOCATION_LIST, eventArgs.upcast());
    }

    /**
     * To set up an event listener, just mixin to your target, call this method with a new event context.
     * See {@link AbstractEvent} for what an actual implementation looks like.
     * @param locations All locations to be executed.
     * @param orderFlipped should event order be flipped when firing?
     * @param eventContext The provided context (of params) through this classes generic.
     */
    void execute(final List<String> locations, final boolean orderFlipped, final Context eventContext);

    /**
     * A default ordered alternative to {@link AbstractEvent#execute(List, boolean, Context)}
     * @param locations All locations to be executed.
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void execute(final List<String> locations, final Context eventContext) {
        execute(locations, false, eventContext);
    }

    /**
     * Generic location alternative to {@link AbstractEvent#execute(List, boolean, Context)}
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void execute(final boolean orderFlipped, final Context eventContext) {
        execute(GENERIC_LOCATION_LIST, orderFlipped, eventContext);
    }

    /**
     * Generic location and default ordered alternative to {@link AbstractEvent#execute(List, boolean, Context)}
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void execute(final Context eventContext) {
        execute(GENERIC_LOCATION_LIST, false, eventContext);
    }
}
