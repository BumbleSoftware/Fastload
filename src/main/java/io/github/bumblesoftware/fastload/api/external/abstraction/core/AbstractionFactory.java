package io.github.bumblesoftware.fastload.api.external.abstraction.core;

import io.github.bumblesoftware.fastload.api.external.abstraction.core.AbstractionHandler.Environment;
import io.github.bumblesoftware.fastload.api.external.abstraction.def.MinecraftAbstractionDirectoryManager;
import io.github.bumblesoftware.fastload.api.external.events.AbstractEvent;
import io.github.bumblesoftware.fastload.util.ObjectHolder;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AbstractionFactory {
    public static <A extends AbstractionApi> AbstractionHandler<A> create(
            final List<String> abstractionModIds,
            final Environment common,
            final Consumer<AbstractEvent<ObjectHolder<A>>> base,
            final Function<A, AbstractionDirectory<A>> abstractionInstanceGetter
    )  {
        return new AbstractionHandler<>(abstractionModIds, common, base, abstractionInstanceGetter);
    }

    public static <A extends AbstractionApi> AbstractionHandler<A> create(
            final List<String> abstractionModIds,
            final Environment common,
            final Consumer<AbstractEvent<ObjectHolder<A>>> base
    ) {
        return new AbstractionHandler<>(abstractionModIds, common, base, MinecraftAbstractionDirectoryManager::new);
    }
}
