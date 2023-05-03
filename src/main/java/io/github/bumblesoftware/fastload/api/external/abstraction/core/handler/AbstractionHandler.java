package io.github.bumblesoftware.fastload.api.external.abstraction.core.handler;

import io.github.bumblesoftware.fastload.api.external.events.AbstractEvent;
import io.github.bumblesoftware.fastload.api.external.events.CapableEvent;
import io.github.bumblesoftware.fastload.util.MutableObjectHolder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.fabricmc.loader.api.FabricLoader.getInstance;

public class AbstractionHandler<A extends MethodAbstractionApi> {
    public final AbstractionDirectory<A> directory;
    public final List<String> abstractionModIds;
    public final Environment environment;
    public final String entrypointName;

    AbstractionHandler(
            final String namespace,
            final List<String> abstractionModIds,
            final Environment environment,
            final Consumer<AbstractEvent<MutableObjectHolder<A>>> base,
            final Function<A, AbstractionDirectory<A>> abstractionInstanceGetter
    ) {
        final AbstractEvent<MutableObjectHolder<A>> event = new CapableEvent<>();
        final MutableObjectHolder<A> abstractionApiHolder = new MutableObjectHolder<>();
        final String entrypointName = namespace.toLowerCase() + "_" + environment.name().toLowerCase();

        base.accept(event);
        getInstance().getEntrypointContainers(entrypointName, AbstractionEntrypoint.class)
                .forEach(container -> abstractionModIds.forEach(abstractionModId -> {
                    if (container.getProvider().getMetadata().getId().equals(abstractionModId)) {
                        container.getEntrypoint().register(event);
                    }
                }));

        event.fire(abstractionApiHolder);

        this.directory = abstractionInstanceGetter.apply(abstractionApiHolder.heldObj);
        this.abstractionModIds = abstractionModIds;
        this.environment = environment;
        this.entrypointName = entrypointName;

        if (abstractionApiHolder.heldObj == null) {
            throw new NullPointerException("Abstraction failed for " + this);
        }
    }

    @Override
    public String toString() {
        return "AbstractionHandler[Env={" + environment +"}, ENTRYPOINT_NAME={" + entrypointName +"}, " +
                "ENTRYPOINT_MODIDS" + "={" + Arrays.toString(abstractionModIds.toArray()) + "}]@" + hashCode();
    }

    public enum Environment {
        CLIENT,
        @SuppressWarnings("unused") COMMON
    }
}
