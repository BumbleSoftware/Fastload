package io.github.bumblesoftware.fastload.abstraction.tool;

import java.util.Arrays;

public interface SupportedVersions {
    String[] getSupportedMinecraftVersions();
    default String getSupportedMinecraftVersionsNonArray() {
        return Arrays.toString(getSupportedMinecraftVersions());
    }
}
