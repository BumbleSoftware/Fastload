package io.github.bumblesoftware.fastload.api.external.abstraction.core.versioning;

import java.util.Arrays;

public interface SupportedVersions {
    String[] getSupportedVersions();
    default String getSupportedMinecraftVersionsNonArray() {
        return Arrays.toString(getSupportedVersions());
    }
}
