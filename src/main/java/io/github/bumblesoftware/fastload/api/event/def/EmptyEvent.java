package io.github.bumblesoftware.fastload.api.event.def;

import io.github.bumblesoftware.fastload.api.event.core.AbstractEvent;
import io.github.bumblesoftware.fastload.api.event.core.EventArgs;
import io.github.bumblesoftware.fastload.api.event.core.EventHolder;
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
    public void clean() {}

    @Override
    public Object2ObjectMap<String, EventHolder<T>> getStorage() {
        return storage;
    }

    @Override
    public List<String> getLocationList() {
        return locationList;
    }

    @Override
    public void removeDynamic(long priority, List<String> locations, EventArgs<T> eventArgs) {}

    @Override
    public void removeStatic(long priority, List<String> locations, EventArgs<T> eventArgs) {}

    @Override
    public void registerDynamic(long priority, List<String> locations, EventArgs<T> argsProvider) {}

    @Override
    public void registerStatic(long priority, List<String> locations, EventArgs<T> argsProvider) {}

    @Override
    public void execute(List<String> locations, boolean orderFlipped, T eventContext) {}
}
