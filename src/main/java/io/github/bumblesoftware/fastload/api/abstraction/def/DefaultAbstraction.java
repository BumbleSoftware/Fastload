package io.github.bumblesoftware.fastload.api.abstraction.def;

import io.github.bumblesoftware.fastload.api.abstraction.core.handler.AbstractionDirectory;
import io.github.bumblesoftware.fastload.api.abstraction.core.handler.MethodAbstractionApi;
import io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionUtil;

public class DefaultAbstraction<A extends MethodAbstractionApi> implements AbstractionDirectory<A> {
    private final VersionUtil versionUtil;
    private final A abstractedClient;

    public DefaultAbstraction(A clientCalls, VersionUtil versionUtil) {
        abstractedClient = clientCalls;
        this.versionUtil = versionUtil;
    }

    @Override
    public VersionUtil getVersionUtil() {
        return versionUtil;
    }

    @Override
    public A getAbstractedEntries() {
        return abstractedClient;
    }
}
