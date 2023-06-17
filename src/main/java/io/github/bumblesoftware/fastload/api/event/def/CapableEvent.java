package io.github.bumblesoftware.fastload.api.event.def;

import io.github.bumblesoftware.fastload.api.event.core.AbstractEvent;
import io.github.bumblesoftware.fastload.api.event.core.EventArgs;
import io.github.bumblesoftware.fastload.api.event.core.EventHolder;
import io.github.bumblesoftware.fastload.api.event.core.EventStatus;
import io.github.bumblesoftware.fastload.client.FLClientEvents;
import io.github.bumblesoftware.fastload.util.obj_holders.MutableObjectHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

import static io.github.bumblesoftware.fastload.api.event.core.EventStatus.*;


/**
 * An event class that provides all the necessary tools to create the exact event you need.
 * @param <Context> used for custom event params. Refer to {@link FLClientEvents FLClientEvents}
 *           for examples.
 */
public class CapableEvent<Context> implements AbstractEvent<Context> {
    private final Comparator<Long> order;
    public final List<String> locationList;
    public final Object2ObjectOpenHashMap<String, EventHolder<Context>>
            allEvents,
            eventsToAdd,
            eventsToRemove;


    public CapableEvent(final Comparator<Long> eventOrder) {
        order = eventOrder;
        locationList = new ArrayList<>();
        allEvents = new Object2ObjectOpenHashMap<>();
        eventsToAdd = new Object2ObjectOpenHashMap<>();
        eventsToRemove = new Object2ObjectOpenHashMap<>();
        allEvents.put(GENERIC_LOCATION, getNewHolder());
        eventsToAdd.put(GENERIC_LOCATION, getNewHolder());
        eventsToRemove.put(GENERIC_LOCATION, getNewHolder());
    }

    public CapableEvent() {
        this(Comparator.reverseOrder());
    }

    @Override
    public synchronized void clean() {
        eventsToAdd.clear();
        eventsToRemove.clear();
        for (final var location : getLocationList()) {
            final var eventHolder = getStorage().get(location);
            if (eventHolder == null)
                getStorage().remove(location);
            else {
                final List<Long> emptyPriorities = new ArrayList<>();
                for (final var priority : eventHolder.priorityHolder()) {
                    if (eventHolder.argsHolder().get(priority.longValue()).isEmpty())
                        emptyPriorities.add(priority);
                }
                for (final var priority : emptyPriorities) {
                    eventHolder.priorityHolder().remove(priority);
                    eventHolder.argsHolder().remove(priority.longValue());
                }
            }
        }
    }

    @Override
    public synchronized Object2ObjectOpenHashMap<String, EventHolder<Context>> getStorage() {
        return allEvents;
    }

    @Override
    public synchronized List<String> getLocationList() {
        return locationList;
    }

    @Override
    public synchronized void removeDynamic(
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
    public synchronized void removeStatic(
            final long priority,
            final List<String> locations,
            final EventArgs<Context> eventArgs
    ) {
        for (final var string : locations) {
            final var holder = getStorage().get(string);
            holder.argsHolder().get(priority).remove(eventArgs);
            if (holder.argsHolder().get(priority).isEmpty()) {
                holder.argsHolder().remove(priority);
                holder.priorityHolder().remove(priority);
            }

        }
    }

    @Override
    public synchronized void registerDynamic(
            final long priority,
            final List<String> locations,
            final EventArgs<Context> eventArgs
    ) {
        for (final var location : locations) {
            eventsToAdd.putIfAbsent(location, getNewHolder());
            final var holder = eventsToAdd.get(location);

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
    public synchronized void registerStatic(
            final long priority,
            final List<String> locations,
            final EventArgs<Context> eventArgs
    ) {
        for (final var location : locations) {
            allEvents.putIfAbsent(location, getNewHolder());
            final EventHolder<Context> holder = allEvents.get(location);
            final var priHol = holder.priorityHolder();
            final var argHol = holder.argsHolder();

            if (!locationList.contains(location))
                locationList.add(location);
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

    private void iterate(EventHolder<Context> holder, BiConsumer<Long, EventArgs<Context>> argsIterator) {
        if (holder != null)
            for (long priority : holder.priorityHolder())
                for (EventArgs<Context> arg : holder.argsHolder().get(priority))
                    if (arg != null)
                        argsIterator.accept(priority, arg);
    }

    @Override
    public void execute(final List<String> locations, final boolean orderFlipped, final Context eventContext) {
        MutableObjectHolder<EventStatus> statusHolder = new MutableObjectHolder<>(CONTINUE);
        for (final var string : locations) {
            final var add = eventsToAdd.get(string);
            final var all = allEvents.get(string);
            final var remove = eventsToRemove.remove(string);

            final Comparator<Long> order;
            if (orderFlipped)
                order = this.order.reversed();
            else order = this.order;

            iterate(add, this::registerStatic);
            all.priorityHolder().sort(order);

            for (long priority : all.priorityHolder()) {
                for (EventArgs<Context> arg : all.argsHolder().get(priority)) {
                    if (arg != null)
                        arg.recursive(eventContext,  statusHolder, this, 0, arg);
                    if (statusHolder.equalsAny(List.of(FINISH_ALL, FINISH_LOCATION, FINISH_PRIORITY)))
                        break;
                }
                if (statusHolder.equalsAny(List.of(FINISH_ALL, FINISH_LOCATION)))
                    break;
            }

            if (statusHolder.getHeldObj().equals(FINISH_ALL))
                break;

            iterate(remove, this::removeStatic);
        }
        clean();
    }
}