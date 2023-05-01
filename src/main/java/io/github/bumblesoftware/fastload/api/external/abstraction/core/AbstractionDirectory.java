package io.github.bumblesoftware.fastload.api.external.abstraction.core;

import io.github.bumblesoftware.fastload.api.external.abstraction.def.MinecraftVersion;
import io.github.bumblesoftware.fastload.api.external.abstraction.tool.version.SupportedVersions;
import io.github.bumblesoftware.fastload.api.external.abstraction.tool.version.VersionUtil.GameSpecific;

public interface AbstractionDirectory<T extends AbstractionApi> extends MinecraftVersion, SupportedVersions {
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
