package io.github.bumblesoftware.fastload.api.external.events;

import io.github.bumblesoftware.fastload.client.FLClientEvents;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


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
    public void clean() {
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
    public Object2ObjectOpenHashMap<String, EventHolder<Context>> getStorage() {
        return allEvents;
    }

    @Override
    public List<String> getLocationList() {
        return locationList;
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
    public void removeThreadUnsafe(
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
    public void registerThreadsafe(
            final long priority,
            final List<String> locations,
            final ArgsProvider<Context> argsProvider
    ) {
        for (final var location : locations) {
            eventsToAdd.putIfAbsent(location, getNewHolder());
            final var holder = eventsToAdd.get(location);
            final var eventArgs = argsProvider.getEvent(this);

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
            final ArgsProvider<Context> argsProvider
    ) {
        for (final var location : locations) {
            allEvents.putIfAbsent(location, getNewHolder());
            final EventHolder<Context> holder = allEvents.get(location);
            final var priHol = holder.priorityHolder();
            final var argHol = holder.argsHolder();
            final var eventArgs = argsProvider.getEvent(this);


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

    private void iterate(EventHolder<Context> holder, ArgsIterator<Context> argsIterator) {
        if (holder != null)
            for (long priority : holder.priorityHolder())
                for (EventArgs<Context> arg : holder.argsHolder().get(priority))
                    if (arg != null)
                        argsIterator.onElement(priority, arg);
    }

    @Override
    public void fire(final List<String> locations, final boolean orderFlipped, final Context eventContext) {
        for (final var string : locations) {
            final var add = eventsToAdd.get(string);
            final var all = allEvents.get(string);
            final var remove = eventsToRemove.remove(string);

            final Comparator<Long> order;
            if (orderFlipped)
                order = this.order.reversed();
            else order = this.order;

            iterate(add, (priority, eventArgs) -> registerThreadUnsafe(priority, event -> eventArgs));
            all.priorityHolder().sort(order);
            iterate(all, (priority, eventArgs) -> eventArgs.recursive(eventContext, this, 0, eventArgs));
            iterate(remove, this::removeThreadUnsafe);
        }
        clean();
    }

    private interface ArgsIterator<Ctx> {
        void onElement(final long priority, final EventArgs<Ctx> eventArgs);
    }
}