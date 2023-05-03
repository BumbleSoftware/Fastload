package io.github.bumblesoftware.fastload.api.abstraction.core.handler;

import io.github.bumblesoftware.fastload.api.abstraction.core.versioning.SupportedVersions;
import io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionProvider;
import io.github.bumblesoftware.fastload.api.abstraction.core.versioning.VersionUtil.GameSpecific;

public interface AbstractionDirectory<T extends MethodAbstractionApi> extends VersionProvider, SupportedVersions {
    @Override
    default String getVersion() {
        return getVersionUtil().providedVersion;
    }

    @Override
    default String[] getSupportedVersions() {
        return getAbstractedEntries().getSupportedVersions();
    }

    GameSpecific getVersionUtil();

    T getAbstractedEntries();
}
