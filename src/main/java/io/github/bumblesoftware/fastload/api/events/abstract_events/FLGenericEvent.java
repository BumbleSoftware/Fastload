package io.github.bumblesoftware.fastload.api.events.abstract_events;

import java.util.ArrayList;

/**
 * Implementations AbstractEvent and allows for a record to be parsed as the type so that the params are 100% dynamic
 */
public final class FLGenericEvent<T extends Record> implements AbstractEvent<T> {
    private final ArrayList<AbstractEvent.EventArgs<T>> EVENT_HOLDER = new ArrayList<>();
    @Override
    public ArrayList<EventArgs<T>> getEventHolder() {
        return EVENT_HOLDER;
    }

}
