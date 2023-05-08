package io.github.bumblesoftware.fastload.api.abstraction.core.handler;

import io.github.bumblesoftware.fastload.api.abstraction.core.handler.AbstractionHandler.Environment;
import io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionUtil;
import io.github.bumblesoftware.fastload.api.abstraction.def.DefaultAbstraction;
import io.github.bumblesoftware.fastload.api.abstraction.def.VersionUtils;
import io.github.bumblesoftware.fastload.api.event.core.AbstractEvent;
import io.github.bumblesoftware.fastload.util.obj_holders.MutableObjectHolder;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class AbstractionFactory {
    public static <A extends MethodAbstractionApi> AbstractionHandler<A> create(
            final String namespace,
            final List<String> abstractionModIds,
            final Environment environment,
            final Consumer<AbstractEvent<MutableObjectHolder<A>>> baseAbstraction,
            final Consumer<AbstractEvent<VersionUtil>> baseClassLoad,
            final VersionUtil versionUtil,
            final BiFunction<A, VersionUtil, AbstractionDirectory<A>> abstractionInstanceGetter
    )  {
        return new AbstractionHandler<>(
                namespace,
                abstractionModIds,
                environment,
                baseAbstraction,
                baseClassLoad,
                versionUtil,
                abstractionInstanceGetter
        );
    }

    public static <A extends MethodAbstractionApi> AbstractionHandler<A> create(
            final String namespace,
            final List<String> abstractionModIds,
            final Environment environment,
            final Consumer<AbstractEvent<MutableObjectHolder<A>>> baseAbstraction,
            final Consumer<AbstractEvent<VersionUtil>> baseClassLoad,
            final VersionUtil versionUtil
    ) {
        return new AbstractionHandler<>(
                namespace,
                abstractionModIds,
                environment,
                baseAbstraction,
                baseClassLoad,
                versionUtil,
                DefaultAbstraction::new
        );
    }

    public static <A extends MethodAbstractionApi> AbstractionHandler<A> create(
            final String namespace,
            final List<String> abstractionModIds,
            final Environment environment,
            final Consumer<AbstractEvent<MutableObjectHolder<A>>> baseAbstraction,
            final Consumer<AbstractEvent<VersionUtil>> baseClassLoad
    ) {
        return create(
                namespace,
                abstractionModIds,
                environment,
                baseAbstraction,
                baseClassLoad,
                VersionUtils.MINECRAFT
        );
    }
}
