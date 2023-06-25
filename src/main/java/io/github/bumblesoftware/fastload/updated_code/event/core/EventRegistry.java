package io.github.bumblesoftware.fastload.updated_code.event.core;

import java.util.Comparator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

public class EventRegistry<Context>{

    public final Map<String, ConcurrentSkipListMap<Long, Queue<EventArgs<Context>>>> args;
    public final Comparator<Long> comparator;

    public EventRegistry() {
        this(Comparator.reverseOrder());
    }

    public EventRegistry(Comparator<Long> comparator) {
        this.comparator = comparator;
        args = new ConcurrentHashMap<>();
    }

    public boolean isEmpty() {
        if (args.isEmpty())
            return true;
        else {
            for (final var priorityMap : args.values()) {
                if (!isEmpty(priorityMap)) {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isEmpty(ConcurrentSkipListMap<Long, Queue<EventArgs<Context>>> priorityHandler) {
        if (priorityHandler == null)
            return true;
        if (priorityHandler.isEmpty())
            return true;
        else {
            for (final var eventArgs : priorityHandler.values()) {
                if (!isEmpty(eventArgs))
                    return false;
            }
        }
        return false;
    }

    public boolean isEmpty(Queue<EventArgs<Context>> priorities) {
        if (priorities == null)
            return true;
        else return priorities.isEmpty();
    }

    public void clean() {
        args.forEach((location, priorityMap) -> {
            priorityMap.forEach((priority, eventArgs) -> {
                if (isEmpty(eventArgs)) {
                    priorityMap.remove(priority, eventArgs);
                }
            });
            if (isEmpty(priorityMap))
                args.remove(location);
        });
    }

    public void remove(String location, long priority, EventArgs<Context> eventArgs) {
        args.get(location).get(priority).remove(eventArgs);
        clean();
    }

    public void add(String location, long priority, EventArgs<Context> eventArgs) {
        args.putIfAbsent(location, new ConcurrentSkipListMap<>());
        add(args.get(location), priority, eventArgs);
    }

    public void add(
            ConcurrentSkipListMap<Long, Queue<EventArgs<Context>>> priorityMap,
            long priority,
            EventArgs<Context> eventArgs
    ) {
        priorityMap.putIfAbsent(priority, new ConcurrentLinkedQueue<>());
        priorityMap.get(priority).add(eventArgs);
    }


    public void addAll(EventRegistry<Context> eventRegistry) {
        eventRegistry.args.forEach((location, priorityMap) ->
                priorityMap.forEach((priority, eventArgs) ->
                        addAll(location, priority, eventArgs)
        ));
    }

    public void addAll(String location, long priority, Queue<EventArgs<Context>> eventArgs) {
        args.putIfAbsent(location, new ConcurrentSkipListMap<>());
        addAll(args.get(location), priority, eventArgs);
    }

    public void addAll(
            ConcurrentSkipListMap<Long, Queue<EventArgs<Context>>> priorityMap,
            long priority,
            Queue<EventArgs<Context>> eventArgs
    ) {
        priorityMap.putIfAbsent(priority, eventArgs);
        priorityMap.get(priority).addAll(eventArgs);
    }

    @Override
    public String toString() {
        return "EventRegistry[" + args +"]@" + hashCode();
    }
}
