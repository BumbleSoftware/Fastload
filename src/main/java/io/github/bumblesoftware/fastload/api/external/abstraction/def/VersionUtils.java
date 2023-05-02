package io.github.bumblesoftware.fastload.api.external.abstraction.def;

import io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning.VersionUtil.GameSpecific;
import io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning.VersionUtil.VersionPackage;

import static io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning.VersionUtil.ExceptionStrategy.NO_EXCEPTION;
import static io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning.VersionUtil.MatchingStrategy.EQUALS;

public class VersionUtils {
    public static final GameSpecific MINECRAFT = new GameSpecific(
            "minecraft",
            VersionPackage::ofFmjVersion,
            EQUALS,
            NO_EXCEPTION
    );
}
