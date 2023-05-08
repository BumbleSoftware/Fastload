package io.github.bumblesoftware.fastload.api.abstraction.core.handler;

import io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionUtil;
import io.github.bumblesoftware.fastload.api.events.AbstractEvent;
import io.github.bumblesoftware.fastload.api.events.CapableEvent;
import io.github.bumblesoftware.fastload.util.MutableObjectHolder;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

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
            final Consumer<AbstractEvent<MutableObjectHolder<A>>> baseAbstraction,
            final Consumer<AbstractEvent<VersionUtil>> baseClassLoad,
            final VersionUtil versionUtil,
            final BiFunction<A, VersionUtil, AbstractionDirectory<A>> abstractionInstanceGetter
    ) {
        final AbstractEvent<MutableObjectHolder<A>> abstractionEvent = new CapableEvent<>();
        final AbstractEvent<VersionUtil> classLoadEvent = new CapableEvent<>();
        final MutableObjectHolder<A> abstractionApiHolder = new MutableObjectHolder<>();
        final String entrypointName = namespace.toLowerCase() + "_" + environment.name().toLowerCase();

        baseAbstraction.accept(abstractionEvent);
        baseClassLoad.accept(classLoadEvent);

        getInstance().getEntrypointContainers(entrypointName, AbstractionEntrypoint.class)
                .forEach(container -> abstractionModIds.forEach(abstractionModId -> {
                    if (container.getProvider().getMetadata().getId().equals(abstractionModId)) {
                        final var entrypoint = container.getEntrypoint();
                        entrypoint.registerAbstraction(abstractionEvent);
                        entrypoint.loadClasses(classLoadEvent);
                    }
                }));

        classLoadEvent.fire(versionUtil);
        abstractionEvent.fire(abstractionApiHolder);

        this.directory = abstractionInstanceGetter.apply(abstractionApiHolder.heldObj, versionUtil);
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
