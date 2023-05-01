package io.github.bumblesoftware.fastload.api.external.abstraction.def;

import io.github.bumblesoftware.fastload.api.external.abstraction.core.AbstractionApi;
import io.github.bumblesoftware.fastload.api.external.abstraction.core.AbstractionDirectory;
import io.github.bumblesoftware.fastload.api.external.abstraction.tool.version.VersionUtil;
import io.github.bumblesoftware.fastload.api.external.abstraction.tool.version.VersionUtil.GameSpecific;

import static io.github.bumblesoftware.fastload.api.external.abstraction.tool.version.VersionUtil.ExceptionStrategy.NO_EXCEPTION;
import static io.github.bumblesoftware.fastload.api.external.abstraction.tool.version.VersionUtil.MatchingStrategy.EQUALS;

public class MinecraftAbstractionDirectoryManager<A extends AbstractionApi> implements AbstractionDirectory<A> {
    private final GameSpecific versionUtil;
    private final A abstractedClient;

    public MinecraftAbstractionDirectoryManager(A clientCalls) {
        abstractedClient = clientCalls;
        versionUtil = new GameSpecific(
                "minecraft",
                VersionUtil.VersionPackage::ofFmjVersion,
                EQUALS,
                NO_EXCEPTION
        );
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
