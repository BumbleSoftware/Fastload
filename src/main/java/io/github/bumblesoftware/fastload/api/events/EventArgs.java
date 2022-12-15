package io.github.bumblesoftware.fastload.api.events;

import io.github.bumblesoftware.fastload.events.FLClientEvents;

/**
 * Event args holds a specific implementation that is added to the main registry for {@link EventFactory
 * AbstractEventFactory} to iterate through upon firing.
 * @param <T> used for custom event params. Refer to {@link FLClientEvents FLEvents}
 *           for examples.
 */
@FunctionalInterface
public interface EventArgs<T extends Record> {

    @SuppressWarnings("UnusedReturnValue")
    EventArgs<T> recurse(
            T eventContext,
            EventFactory<T> abstractParent,
            Object closer,
            EventArgs<T> eventArgs
    );
}