package io.github.bumblesoftware.fastload.api.events;

import io.github.bumblesoftware.fastload.client.FLClientEvents;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * A fully capable event with all the tools
 * @param <T> used for custom event params. Refer to {@link FLClientEvents FLClientEvents}
 *           for examples.
 */
public class CapableEvent<T extends Record, T2> implements AbstractEvent<T, T2> {
    public final EventHolder<T, T2>
            allEvents = getNewHolder(),
            eventsToAdd = getNewHolder(),
            eventsToRemove = getNewHolder();

    public final @Nullable SwitchHelper<T, T2> switchHelper;

    public CapableEvent(@Nullable SwitchHelper<T, T2> switchHelper) {
        this.switchHelper = switchHelper;
    }

    public CapableEvent() {
        this(null);
    }

    @Override
    public EventHolder<T, T2> getHolder() {
        return allEvents;
    }

    @Override
    public void removeThreadSafe(final long priority, final EventArgs<T, T2> eventArgs) {
        eventsToRemove.priorityHolder().add(priority);
        eventsToRemove.argsHolder().get(priority).add(eventArgs);
    }

    @Override
    public void registerThreadsafe(long priority, EventArgs<T, T2> eventArgs) {
        eventsToAdd.priorityHolder().add(priority);
        eventsToAdd.argsHolder().get(priority).add(eventArgs);
    }

    @Override
    public void registerThreadUnsafe(final long priority, final EventArgs<T, T2> eventArgs) {
        var priHol = allEvents.priorityHolder();
        if (!priHol.contains(priority))
            priHol.add(priority);
        var argHol = allEvents.argsHolder();
        argHol.putIfAbsent(priority, new ArrayList<>());
        argHol.get(priority).add(eventArgs);
    }

    @Override
    public EventHolder<T, T2> getMultipleArgsHolders(T2 identifier) {
        if (switchHelper == null) {
            throw new UnsupportedOperationException();
        } else return switchHelper.switchWith(identifier);
    }

    private void iterate(EventHolder<T, T2> holder, ArgsIterator<T, T2> argsIterator) {
        for (long priority : holder.priorityHolder()) {
            for (EventArgs<T, T2> arg : holder.argsHolder().get(priority)) {
                if (arg != null) {
                    argsIterator.onElement(priority, arg);
                }
            }
        }
    }

    public void fireEvent(final T eventContext) {
        iterate(eventsToAdd, this::registerThreadUnsafe);
        allEvents.priorityHolder().sort(Comparator.reverseOrder());
        iterate(allEvents, (priority, arg) -> arg.onEvent(eventContext, this, 0, arg));
        iterate(eventsToRemove, this::removeThreadUnsafe);
        eventsToAdd.priorityHolder().clear();
        eventsToAdd.argsHolder().clear();
        eventsToRemove.priorityHolder().clear();
        eventsToRemove.argsHolder().clear();
    }

    private interface ArgsIterator<T extends Record, T2> {
        void onElement(final long priority, final EventArgs<T, T2> arg);
    }

    private interface SwitchHelper<T extends Record, T2> {
        EventHolder<T, T2> switchWith(final T2 identifier);
    }
}