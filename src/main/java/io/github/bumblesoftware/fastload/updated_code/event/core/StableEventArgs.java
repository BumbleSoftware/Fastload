package io.github.bumblesoftware.fastload.updated_code.event.core;

import obj_holders.ObjectHolder;

@FunctionalInterface
public interface StableEventArgs<Context> extends EventArgs<Context> {

    default EventArgs<Context> upcast() {
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
    default EventArgs<Context> recursive(
            final Context eventContext,
            final ObjectHolder<EventStatus> eventStatus,
            final AbstractEvent<Context> event,
            final Object closer,
            final EventArgs<Context> eventArgs
    ) {
        stable(eventContext, eventStatus, event, eventArgs);
        return null;
    }
    /**
     * @param eventContext The events params.
     * @param eventArgs The current instance provided for return (recursion)
     */
    void stable(
            final Context eventContext,
            final ObjectHolder<EventStatus> eventStatus,
            final AbstractEvent<Context> event,
            final EventArgs<Context> eventArgs
    );
}