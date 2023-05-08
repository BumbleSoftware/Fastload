package io.github.bumblesoftware.fastload.api.abstraction.def;

import io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionPackage;
import io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionUtil;

import static io.github.bumblesoftware.fastload.api.abstraction.core.versioning.ExceptionStrategy.NO_EXCEPTION;
import static io.github.bumblesoftware.fastload.api.abstraction.core.versioning.MatchingStrategy.EQUALS;

public class VersionUtils {
    public static final VersionUtil MINECRAFT = new VersionUtil(
            "minecraft",
            VersionPackage::ofFmjVersion,
            EQUALS,
            NO_EXCEPTION
    );
}
