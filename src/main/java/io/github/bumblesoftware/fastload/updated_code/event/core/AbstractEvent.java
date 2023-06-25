package io.github.bumblesoftware.fastload.updated_code.event.core;

import static event.core.EventConstants.GENERIC_LOCATION;


public interface AbstractEvent<Context> {
    default boolean isEmpty() {
        return getRegistry().isEmpty();
    }

    default void clean() {
        getRegistry().clean();
    }

    EventRegistry<Context> getRegistry();



    /**
     * Removes a listener from the event.
     * @param eventArgs The args that will be called when event executes.
     * @param locations All locations that this eventArg should be removed from.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    void removeListener(final long priority, final String locations, final EventArgs<Context> eventArgs);

    /**
     * Generic location alternative to {@link AbstractEvent#removeListener(long, String, EventArgs)}
     * @param eventArgs The args that will be called when event executed.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void removeListener(final long priority, final EventArgs<Context> eventArgs) {
        removeListener(priority, GENERIC_LOCATION, eventArgs);
    }



    /**
     * Registers a listener for this event.
     * @param eventArgs The args that will be called when event executes.
     * @param location Location of event.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    void registerListener(final long priority, final String location, final EventArgs<Context> eventArgs);

    /**
     * StableEventArgs alternative to {@link AbstractEvent#registerListener(long, String, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerListener(final long priority, final String locations, final StableEventArgs<Context> eventArgs) {
        registerListener(priority, locations, eventArgs.upcast());
    }

    /**
     * Generic location alternative to {@link AbstractEvent#registerListener(long, String, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerListener(final long priority, final EventArgs<Context> eventArgs) {
        registerListener(priority, GENERIC_LOCATION, eventArgs);
    }

    /**
     * StableEventArgs alternative to {@link AbstractEvent#registerListener(long, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerListener(final long priority, final StableEventArgs<Context> eventArgs) {
        registerListener(priority, eventArgs.upcast());
    }



    /**
     * To set up an event listener, just mixin to your target, call this method with a new event context.
     * See {@link AbstractEvent} for what an actual implementation looks like.
     * @param location Location of event.
     * @param eventContext The provided context (of params) through this classes generic.
     */
    void execute(final String location, final Context eventContext);

    /**
     * Generic location alternative to {@link AbstractEvent#execute(String, Context)}
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void execute(final Context eventContext) {
        execute(GENERIC_LOCATION, eventContext);
    }
}
