package io.github.bumblesoftware.fastload.api.external.events;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * EmptyEvent is used to debug events by essentially turning them off by assigning them this class.
 */
@SuppressWarnings("unused")
public class EmptyEvent<T> implements AbstractEvent<T> {

    private final List<String> locationList = new ArrayList<>();
    private final Object2ObjectMap<String, EventHolder<T>> storage = new Object2ObjectOpenHashMap<>();

    @Override
    public void clean() {
    }

    @Override
    public Object2ObjectMap<String, EventHolder<T>> getStorage() {
        return storage;
    }

    @Override
    public List<String> getLocationList() {
        return locationList;
    }

    @Override
    public void removeThreadSafe(long priority, List<String> locations, EventArgs<T> eventArgs) {

    }

    @Override
    public void removeThreadUnsafe(long priority, List<String> locations, EventArgs<T> eventArgs) {

    }

    @Override
    public void registerThreadsafe(long priority, List<String> locations, ArgsProvider<T> argsProvider) {

    }

    @Override
    public void registerThreadUnsafe(long priority, List<String> locations, ArgsProvider<T> argsProvider) {

    }

    @Override
    public void fire(List<String> locations, boolean orderFlipped, T eventContext) {

    }
}
