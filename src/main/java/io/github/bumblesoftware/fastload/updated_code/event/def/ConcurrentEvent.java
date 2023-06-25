package io.github.bumblesoftware.fastload.updated_code.event.def;

import event.core.AbstractEvent;
import event.core.EventArgs;
import event.core.EventRegistry;
import event.core.EventStatus;
import obj_holders.MutableObjectHolder;
import obj_holders.ObjectHolder;

import java.util.Comparator;
import java.util.List;

import static event.core.EventStatus.*;


public class ConcurrentEvent<Context> implements AbstractEvent<Context> {
    private final EventRegistry<Context> eventRegistry;

    public ConcurrentEvent(final Comparator<Long> eventOrder) {
        eventRegistry = new EventRegistry<>(eventOrder);
    }

    public ConcurrentEvent() {
        this(Comparator.reverseOrder());
    }

    @Override
    public EventRegistry<Context> getRegistry() {
        return eventRegistry;
    }

    @Override
    public void removeListener(long priority, String location, EventArgs<Context> eventArgs) {
        eventRegistry.remove(location, priority, eventArgs);
    }

    @Override
    public void registerListener(long priority, String location, EventArgs<Context> eventArgs) {
        eventRegistry.add(location, priority, eventArgs);
    }

    @Override
    public void execute(final String location, final Context eventContext) {
        ObjectHolder<EventStatus> statusHolder = new MutableObjectHolder<>(CONTINUE);
        final var all = getRegistry().args.get(location);
        if (all != null)
            for (final var eventArgs : all.values()) {
                for (EventArgs<Context> eventArg : eventArgs) {
                    eventArg.recursive(eventContext, statusHolder, this, 0, eventArg);
                    if (statusHolder.equalsAny(List.of(FINISH_PRIORITY, FINISH_LOCATION)))
                        break;
                }
                if (statusHolder.equalsAny(List.of(FINISH_LOCATION)))
                    break;
            }
    }
}