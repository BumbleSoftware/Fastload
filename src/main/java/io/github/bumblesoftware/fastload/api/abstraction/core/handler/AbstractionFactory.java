package io.github.bumblesoftware.fastload.api.abstraction.core.handler;

import io.github.bumblesoftware.fastload.api.abstraction.core.handler.AbstractionHandler.Environment;
import io.github.bumblesoftware.fastload.api.abstraction.def.MinecraftAbstraction;
import io.github.bumblesoftware.fastload.api.events.AbstractEvent;
import io.github.bumblesoftware.fastload.util.MutableObjectHolder;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AbstractionFactory {
    public static <A extends MethodAbstractionApi> AbstractionHandler<A> create(
            final String namespace,
            final List<String> abstractionModIds,
            final Environment common,
            final Consumer<AbstractEvent<MutableObjectHolder<A>>> base,
            final Function<A, AbstractionDirectory<A>> abstractionInstanceGetter
    )  {
        return new AbstractionHandler<>(namespace, abstractionModIds, common, base, abstractionInstanceGetter);
    }

    public static <A extends MethodAbstractionApi> AbstractionHandler<A> create(
            final String namespace,
            final List<String> abstractionModIds,
            final Environment common,
            final Consumer<AbstractEvent<MutableObjectHolder<A>>> base
    ) {
        return new AbstractionHandler<>(namespace, abstractionModIds, common, base, MinecraftAbstraction::new);
    }
}
