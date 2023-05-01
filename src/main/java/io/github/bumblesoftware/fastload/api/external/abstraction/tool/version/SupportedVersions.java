package io.github.bumblesoftware.fastload.api.external.abstraction.tool.version;

import java.util.Arrays;

public interface SupportedVersions {
    String[] getSupportedVersions();
    default String getSupportedMinecraftVersionsNonArray() {
        return Arrays.toString(getSupportedVersions());
    }
}
