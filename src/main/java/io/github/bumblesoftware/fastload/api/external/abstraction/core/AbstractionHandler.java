package io.github.bumblesoftware.fastload.api.external.abstraction.core;

import io.github.bumblesoftware.fastload.api.external.events.AbstractEvent;
import io.github.bumblesoftware.fastload.api.external.events.CapableEvent;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.ObjectHolder;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.fabricmc.loader.api.FabricLoader.getInstance;

public class AbstractionHandler<A extends AbstractionApi> {
    private static final String NAMESPACE = Fastload.NAMESPACE;
    public final AbstractionDirectory<A> directory;
    public final Environment environment;

    AbstractionHandler(
            final List<String> abstractionModIds,
            final Environment environment,
            final Consumer<AbstractEvent<ObjectHolder<A>>> base,
            final Function<A, AbstractionDirectory<A>> abstractionInstanceGetter
    ) {
        final AbstractEvent<ObjectHolder<A>> event = new CapableEvent<>();
        final ObjectHolder<A> abstraction = new ObjectHolder<>();
        final String entrypointName = NAMESPACE.toUpperCase() + "_" + environment.name().toUpperCase();

        base.accept(event);
        getInstance().getEntrypointContainers(entrypointName, AbstractionEntrypoint.class)
                .forEach(container -> abstractionModIds.forEach(abstractionModId -> {
                    if (container.getProvider().getMetadata().getId().equals(abstractionModId)) {
                        container.getEntrypoint().register(event);
                    }
                }));

        event.fire(abstraction);

        this.directory = abstractionInstanceGetter.apply(abstraction.heldObj);
        this.environment = environment;
    }

    public enum Environment {
        CLIENT,
        COMMON
    }
}
