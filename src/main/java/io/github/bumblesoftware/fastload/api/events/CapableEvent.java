package io.github.bumblesoftware.fastload.api.events;

import io.github.bumblesoftware.fastload.client.FLClientEvents;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * A fully capable event with all the tools
 * @param <Context> used for custom event params. Refer to {@link FLClientEvents FLClientEvents}
 *           for examples.
 */
public class CapableEvent<Context> implements AbstractEvent<Context> {
    public final Object2ObjectOpenHashMap<String,EventHolder<Context>>
            allEvents,
            eventsToAdd,
            eventsToRemove;

    public final @Nullable SwitchHelper<Context> switchHelper;

    public CapableEvent(@Nullable SwitchHelper<Context> switchHelper) {
        this.switchHelper = switchHelper;
        allEvents = new Object2ObjectOpenHashMap<>();
        eventsToAdd = new Object2ObjectOpenHashMap<>();
        eventsToRemove = new Object2ObjectOpenHashMap<>();
        allEvents.put(GENERIC_LOCATION, getNewHolder());
        eventsToAdd.put(GENERIC_LOCATION, getNewHolder());
        eventsToRemove.put(GENERIC_LOCATION, getNewHolder());
    }

    public CapableEvent() {
        this(null);
    }

    @Override
    public Object2ObjectOpenHashMap<String, EventHolder<Context>> getStorage() {
        return allEvents;
    }

    @Override
    public void removeThreadSafe(
            final long priority,
            final List<String> locations,
            final EventArgs<Context> eventArgs
    ) {
        for (final var string : locations) {
            eventsToRemove.putIfAbsent(string, getNewHolder());
            final var holder = eventsToRemove.get(string);
            if (!holder.priorityHolder().contains(priority))
                holder.priorityHolder().add(priority);
            if (!holder.argsHolder().containsKey(priority)) {
                final ArrayList<EventArgs<Context>> list = new ArrayList<>();
                list.add(eventArgs);
                holder.argsHolder().put(priority, list);
            } else if (!holder.argsHolder().get(priority).contains(eventArgs))
                holder.argsHolder().get(priority).add(eventArgs);
        }
    }

    @Override
    public void registerThreadsafe(
            final long priority,
            final List<String> locations,
            final EventArgs<Context> eventArgs
    ) {
        for (final var string : locations) {
            eventsToAdd.putIfAbsent(string, getNewHolder());
            final var holder = eventsToAdd.get(string);
            if (!holder.priorityHolder().contains(priority))
                holder.priorityHolder().add(priority);
            if (!holder.argsHolder().containsKey(priority)) {
                final ArrayList<EventArgs<Context>> list = new ArrayList<>();
                list.add(eventArgs);
                holder.argsHolder().put(priority, list);
            } else if (!holder.argsHolder().get(priority).contains(eventArgs))
                holder.argsHolder().get(priority).add(eventArgs);
        }
    }

    @Override
    public void registerThreadUnsafe(
            final long priority,
            final List<String> locations,
            final EventArgs<Context> eventArgs
    ) {
        for (final var string : locations) {
            allEvents.putIfAbsent(string, getNewHolder());
            final EventHolder<Context> holder = allEvents.get(string);
            final var priHol = holder.priorityHolder();
            final var argHol = holder.argsHolder();

            if (!priHol.contains(priority))
                priHol.add(priority);
            if (!argHol.containsKey(priority)) {
                final ArrayList<EventArgs<Context>> list = new ArrayList<>();
                list.add(eventArgs);
                argHol.put(priority, list);
            } else if (!argHol.get(priority).contains(eventArgs))
                argHol.get(priority).add(eventArgs);
        }
    }

    @Override
    public Object2ObjectOpenHashMap<String, EventHolder<Context>> getMultipleArgsHolders(String identifier) {
        if (switchHelper == null) {
            throw new UnsupportedOperationException();
        } else return switchHelper.switchWith(identifier);
    }

    private void iterate(EventHolder<Context> holder, ArgsIterator<Context> argsIterator) {
        if (holder != null)
            for (long priority : holder.priorityHolder())
                for (EventArgs<Context> arg : holder.argsHolder().get(priority))
                    if (arg != null)
                        argsIterator.onElement(priority, arg);
    }

    @Override
    public void fireEvent(final List<String> locations, final Context eventContext) {
        for (final var string : locations) {
            final var add = eventsToAdd.get(string);
            final var all = allEvents.get(string);
            final var remove = eventsToRemove.remove(string);
            final ArrayList<Long> emptyPriorities = new ArrayList<>();

            iterate(add, this::registerThreadUnsafe);
            all.priorityHolder().sort(Comparator.reverseOrder());
            iterate(all, (priority, arg) -> arg.onEvent(eventContext, this, 0, arg));
            iterate(remove, this::removeThreadUnsafe);

            for (long priority : all.priorityHolder())
                if (all.argsHolder().get(priority).isEmpty()) {
                    all.argsHolder().remove(priority);
                    emptyPriorities.add(priority);
                }
            for (long priority : emptyPriorities)
                all.priorityHolder().remove(priority);

            emptyPriorities.clear();

            if (add != null) {
                add.priorityHolder().clear();
                add.argsHolder().clear();
            }
            if (remove != null) {
                remove.priorityHolder().clear();
                remove.argsHolder().clear();
            }
        }
    }

    private interface ArgsIterator<Ctx> {
        void onElement(final long priority, final EventArgs<Ctx> arg);
    }

    private interface SwitchHelper<Ctx> {
        Object2ObjectOpenHashMap<String, EventHolder<Ctx>> switchWith(final String identifier);
    }
}