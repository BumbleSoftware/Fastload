package io.github.bumblesoftware.fastload.api.abstraction.core.handler;

import io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionUtil;
import io.github.bumblesoftware.fastload.api.event.core.AbstractEvent;
import io.github.bumblesoftware.fastload.api.event.def.CapableEvent;
import io.github.bumblesoftware.fastload.util.obj_holders.MutableObjectHolder;

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

        classLoadEvent.execute(versionUtil);
        abstractionEvent.execute(abstractionApiHolder);

        this.directory = abstractionInstanceGetter.apply(abstractionApiHolder.getHeldObj(), versionUtil);
        this.abstractionModIds = abstractionModIds;
        this.environment = environment;
        this.entrypointName = entrypointName;

        if (abstractionApiHolder.getHeldObj() == null) {
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
