package com.abdelaziz.fastload.api.events;

import com.abdelaziz.fastload.api.events.EventProviders.*;
import com.abdelaziz.fastload.client.FLClientEvents;

/**
 * An extension of {@link EventFactory} which adds builders to override methods with custom implementations via
 * {@link EventProviders}
 *
 * @param <T> used for custom event params. Refer to {@link FLClientEvents FLEvents}
 *            for examples.
 */
@SuppressWarnings("unused")
public class EventFactoryBuilder<T extends Record> extends EventFactory<T> {
    private MultipleArgsHolders<T> MULTIPLE_ARGS_PROVIDER = null;
    private DynamicallyRemoveEvent<T> DYNAMIC_EVENT_REMOVE_PROVIDER = null;
    private StaticallyRemoveEvent<T> STATIC_EVENT_REMOVE_PROVIDER = null;
    private RegisterEvent<T> REGISTRY_EVENT_PROVIDER = null;
    private FireEvent<T> FIRE_EVENT_PROVIDER = null;

    public EventFactoryBuilder<T> setArgsProvider(MultipleArgsHolders<T> provider) {
        MULTIPLE_ARGS_PROVIDER = provider;
        return this;
    }

    public EventFactoryBuilder<T> setDynamicRemoveProvider(DynamicallyRemoveEvent<T> provider) {
        DYNAMIC_EVENT_REMOVE_PROVIDER = provider;
        return this;
    }

    public EventFactoryBuilder<T> setStaticRemoveProvider(StaticallyRemoveEvent<T> provider) {
        STATIC_EVENT_REMOVE_PROVIDER = provider;
        return this;
    }

    public EventFactoryBuilder<T> setRegistryProvider(RegisterEvent<T> provider) {
        REGISTRY_EVENT_PROVIDER = provider;
        return this;
    }

    public EventFactoryBuilder<T> setFireEventProvider(FireEvent<T> provider) {
        FIRE_EVENT_PROVIDER = provider;
        return this;
    }

    @Override
    public EventHolder<T> getMultipleArgsHolders(String identifier) {
        if (MULTIPLE_ARGS_PROVIDER == null) {
            return super.getMultipleArgsHolders(identifier);
        } else {
            return MULTIPLE_ARGS_PROVIDER.getMultipleArgsHolders(identifier);
        }
    }

    @Override
    public void dynamicallyRemoveEvent(long priority, EventArgs<T> eventArgs) {
        if (DYNAMIC_EVENT_REMOVE_PROVIDER == null) {
            super.dynamicallyRemoveEvent(priority, eventArgs);
        } else {
            DYNAMIC_EVENT_REMOVE_PROVIDER.dynamicallyRemoveEvent(priority, eventArgs);
        }
    }

    @Override
    public void staticallyRemoveEvent(long priority, EventArgs<T> eventArgs) {
        if (STATIC_EVENT_REMOVE_PROVIDER == null) {
            super.staticallyRemoveEvent(priority, eventArgs);
        } else {
            STATIC_EVENT_REMOVE_PROVIDER.staticallyRemoveEvent(priority, eventArgs);
        }
    }

    @Override
    public void register(long priority, EventArgs<T> eventArgs) {
        if (REGISTRY_EVENT_PROVIDER == null) {
            super.register(priority, eventArgs);
        } else {
            REGISTRY_EVENT_PROVIDER.register(priority, eventArgs);
        }
    }

    @Override
    public void fireEvent(T eventContext) {
        if (FIRE_EVENT_PROVIDER == null) {
            super.fireEvent(eventContext);
        } else {
            FIRE_EVENT_PROVIDER.fireEvent(eventContext);
        }
    }
}
