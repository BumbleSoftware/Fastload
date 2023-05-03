package io.github.bumblesoftware.fastload.api.abstraction.core.versioning;

import java.util.Arrays;

public interface SupportedVersions {
    String[] getSupportedVersions();
    default String getSupportedVersionsNonArray() {
        return Arrays.toString(getSupportedVersions());
    }
}
