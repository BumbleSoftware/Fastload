package io.github.bumblesoftware.fastload.api.external.abstraction.client;

import io.github.bumblesoftware.fastload.api.external.abstraction.tool.version.AbstractionManager;
import io.github.bumblesoftware.fastload.version.VersionUtil;

import static io.github.bumblesoftware.fastload.version.VersionUtil.ExceptionStrategy.NO_EXCEPTION;
import static io.github.bumblesoftware.fastload.version.VersionUtil.MatchingStrategy.EQUALS;

public class AbstractionManagerImpl<A extends AbstractionApi> implements AbstractionManager<A> {
    private final VersionUtil.GameSpecific versionUtil;
    private final A abstractedClient;

    public AbstractionManagerImpl(A clientCalls) {
        abstractedClient = clientCalls;
        versionUtil = new VersionUtil.GameSpecific(
                "minecraft",
                VersionUtil.VersionPackage::ofFmjVersion,
                EQUALS,
                NO_EXCEPTION
        );
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
