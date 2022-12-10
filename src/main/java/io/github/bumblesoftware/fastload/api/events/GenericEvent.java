package io.github.bumblesoftware.fastload.api.events;

import java.util.ArrayList;

/**
 * Implements AbstractEvent and allows for a record to be parsed as the type so that the params are 100% dynamic.
 * In order to register your params, just make a record with a header containing your desired params. Then you simply need to
 * register this object with your custom record as the generic type, and there's your custom event!
 */
public final class GenericEvent<T extends Record> implements AbstractEvent<T> {
    /**
     * Simple field that stores an implementation of the args() method from the EventArgs interface. Use
     * Register() to add your implementation to the arraylist of implementations that get iterated and called
     * upon the event firing.
     */
    private final ArrayList<EventArgs<T>> EVENT_HOLDER = new ArrayList<>();

    /**
     * Simply provides the necessary field so that the interface can access it. It is not in there
     * so that it can be private, prevented people from mucking around with it and breaking stuff.
     * The field is not supposed be accessed. Plus fields have to be static when in an interface, which doesn't
     * work with the way this event is designed.
     */
    @Override
    public ArrayList<EventArgs<T>> getEventHolder() {
        return EVENT_HOLDER;
    }
}
