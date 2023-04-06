package io.github.bumblesoftware.fastload.api.events;

import io.github.bumblesoftware.fastload.client.FLClientEvents;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * A fully capable event with all the tools
 * @param <Context> used for custom event params. Refer to {@link FLClientEvents FLClientEvents}
 *           for examples.
 */
public class CapableEvent<Context> implements AbstractEvent<Context> {
    public final EventHolder<Context>
            allEvents = getNewHolder(),
            eventsToAdd = getNewHolder(),
            eventsToRemove = getNewHolder();

    public final @Nullable SwitchHelper<Context> switchHelper;

    public CapableEvent(@Nullable SwitchHelper<Context> switchHelper) {
        this.switchHelper = switchHelper;
    }

    public CapableEvent() {
        this(null);
    }

    @Override
    public EventHolder<Context> getHolder() {
        return allEvents;
    }

    @Override
    public void removeThreadSafe(final long priority, final EventArgs<Context> eventArgs) {
        eventsToRemove.priorityHolder().add(priority);
        eventsToRemove.argsHolder().get(priority).add(eventArgs);
    }

    @Override
    public void registerThreadsafe(long priority, EventArgs<Context> eventArgs) {
        eventsToAdd.priorityHolder().add(priority);
        eventsToAdd.argsHolder().get(priority).add(eventArgs);
    }

    @Override
    public void registerThreadUnsafe(final long priority, final EventArgs<Context> eventArgs) {
        var priHol = allEvents.priorityHolder();
        if (!priHol.contains(priority))
            priHol.add(priority);
        var argHol = allEvents.argsHolder();
        argHol.putIfAbsent(priority, new ArrayList<>());
        argHol.get(priority).add(eventArgs);
    }

    @Override
    public EventHolder<Context> getMultipleArgsHolders(String identifier) {
        if (switchHelper == null) {
            throw new UnsupportedOperationException();
        } else return switchHelper.switchWith(identifier);
    }

    private void iterate(EventHolder<Context> holder, ArgsIterator<Context> argsIterator) {
        for (long priority : holder.priorityHolder()) {
            for (EventArgs<Context> arg : holder.argsHolder().get(priority)) {
                if (arg != null) {
                    argsIterator.onElement(priority, arg);
                }
            }
        }
    }

    @Override
    public void fireEvent(final Context eventContext) {
        iterate(eventsToAdd, this::registerThreadUnsafe);
        allEvents.priorityHolder().sort(Comparator.reverseOrder());
        iterate(allEvents, (priority, arg) -> arg.onEvent(eventContext, this, 0, arg));
        iterate(eventsToRemove, this::removeThreadUnsafe);
        eventsToAdd.priorityHolder().clear();
        eventsToAdd.argsHolder().clear();
        eventsToRemove.priorityHolder().clear();
        eventsToRemove.argsHolder().clear();
    }

    private interface ArgsIterator<Ctx> {
        void onElement(final long priority, final EventArgs<Ctx> arg);
    }

    private interface SwitchHelper<Ctx> {
        EventHolder<Ctx> switchWith(final String identifier);
    }
}