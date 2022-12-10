package io.github.bumblesoftware.fastload.api.events.custom;

import java.util.ArrayList;

public final class FLAbstractEvent {
    protected interface AbstractEvent<T extends EventArgs> {
        ArrayList<T> getEventHolder();
        default void register(T event) {
            getEventHolder().add(event);
        }
    }
    protected interface EventArgs {}
}
