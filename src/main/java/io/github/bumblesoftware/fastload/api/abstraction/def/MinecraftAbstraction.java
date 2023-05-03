package io.github.bumblesoftware.fastload.api.abstraction.def;

import io.github.bumblesoftware.fastload.api.abstraction.core.handler.AbstractionDirectory;
import io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionUtil;
import io.github.bumblesoftware.fastload.api.abstraction.core.handler.MethodAbstractionApi;

public class MinecraftAbstraction<A extends MethodAbstractionApi> implements AbstractionDirectory<A> {
    private final VersionUtil.GameSpecific versionUtil;
    private final A abstractedClient;

    public MinecraftAbstraction(A clientCalls) {
        abstractedClient = clientCalls;
        versionUtil = VersionUtils.MINECRAFT;
    }

    @Override
    public VersionUtil.GameSpecific getVersionUtil() {
        return versionUtil;
    }

    @Override
    public A getAbstractedEntries() {
        return abstractedClient;
    }
}
