package io.github.bumblesoftware.fastload.updated_code.event.core;

import obj_holders.ObjectHolder;

@FunctionalInterface
public interface EventArgs<Context> {

    /**
     * @param eventContext The events params.
     * @param event The event instance for managerial purposes.
     * @param closer The instance responsible for holding the value that will close this loop.
     * @param eventArgs The current instance provided for return (recursion)
     * @return The current instance.
     */
    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    EventArgs<Context> recursive(
            final Context eventContext,
            final ObjectHolder<EventStatus> statusHolder,
            final AbstractEvent<Context> event,
            final Object closer,
            final EventArgs<Context> eventArgs
    );
}