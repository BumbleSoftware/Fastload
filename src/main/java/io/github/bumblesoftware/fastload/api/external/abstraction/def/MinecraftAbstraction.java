package io.github.bumblesoftware.fastload.api.external.abstraction.def;

import io.github.bumblesoftware.fastload.api.external.abstraction.core.handler.AbstractionDirectory;
import io.github.bumblesoftware.fastload.api.external.abstraction.core.handler.MethodAbstractionApi;
import io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning.VersionUtil.GameSpecific;

public class MinecraftAbstraction<A extends MethodAbstractionApi> implements AbstractionDirectory<A> {
    private final GameSpecific versionUtil;
    private final A abstractedClient;

    public MinecraftAbstraction(A clientCalls) {
        abstractedClient = clientCalls;
        versionUtil = VersionUtils.MINECRAFT;
    }

    @Override
    public GameSpecific getVersionUtil() {
        return versionUtil;
    }

    @Override
    public A getAbstractedEntries() {
        return abstractedClient;
    }
}
