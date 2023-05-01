package io.github.bumblesoftware.fastload.api.external.abstraction.management;

import io.github.bumblesoftware.fastload.api.external.abstraction.client.AbstractionApi;
import io.github.bumblesoftware.fastload.api.external.abstraction.client.AbstractionManagerImpl;
import io.github.bumblesoftware.fastload.api.external.abstraction.tool.version.AbstractionManager;
import io.github.bumblesoftware.fastload.api.external.events.AbstractEvent;
import io.github.bumblesoftware.fastload.api.external.events.CapableEvent;
import io.github.bumblesoftware.fastload.util.ObjectHolder;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.fabricmc.loader.api.FabricLoader.getInstance;

public class AbstractionHandler<A extends AbstractionApi> {

    public final AbstractionManager<A> manager;

    public AbstractionHandler(
            final List<String> abstractionModIds,
            final Consumer<AbstractEvent<ObjectHolder<A>>> base,
            final Function<A, AbstractionManager<A>> abstractionInstanceGetter
    ) {
        final AbstractEvent<ObjectHolder<A>> event = new CapableEvent<>();
        final ObjectHolder<A> abstraction = new ObjectHolder<>();

        base.accept(event);
        getInstance().getEntrypointContainers("fastload", AbstractionEntrypoint.class)
                .forEach(container -> abstractionModIds.forEach(abstractionModId -> {
                    if (container.getProvider().getMetadata().getId().equals(abstractionModId)) {
                        container.getEntrypoint().register(event);
                    }
                }));

        event.fire(abstraction);

        this.manager = abstractionInstanceGetter.apply(abstraction.heldObj);
    }

    public AbstractionHandler(
            final List<String> abstractionModIds,
            final Consumer<AbstractEvent<ObjectHolder<A>>> base
    ) {
        this(abstractionModIds, base, AbstractionManagerImpl::new);
    }
}
