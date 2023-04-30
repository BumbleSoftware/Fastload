package io.github.bumblesoftware.fastload.abstraction.client;

import io.github.bumblesoftware.fastload.abstraction.tool.MinecraftAbstraction;
import io.github.bumblesoftware.fastload.version.VersionUtil;

import static io.github.bumblesoftware.fastload.version.VersionUtil.ExceptionStrategy.NO_EXCEPTION;
import static io.github.bumblesoftware.fastload.version.VersionUtil.MatchingStrategy.EQUALS;

public class MinecraftAbstractionImpl implements MinecraftAbstraction<AbstractClientCalls> {
    private final VersionUtil.GameSpecific VERSION_UTIL;

    private final AbstractClientCalls ABSTRACTED_CLIENT;

    public MinecraftAbstractionImpl(AbstractClientCalls clientCalls) {
        ABSTRACTED_CLIENT = clientCalls;
        VERSION_UTIL = new VersionUtil.GameSpecific(
                "minecraft",
                VersionUtil.VersionPackage::ofFmjVersion,
                EQUALS,
                NO_EXCEPTION
        );
    }

    @Override
    public VersionUtil.GameSpecific getMinecraftVersionUtil() {
        return VERSION_UTIL;
    }

    @Override
    public AbstractClientCalls getAbstractedEntries() {
        return ABSTRACTED_CLIENT;
    }
}
