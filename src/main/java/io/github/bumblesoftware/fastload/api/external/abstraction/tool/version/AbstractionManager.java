package io.github.bumblesoftware.fastload.api.external.abstraction.tool.version;

import io.github.bumblesoftware.fastload.api.external.abstraction.client.AbstractionApi;
import io.github.bumblesoftware.fastload.version.VersionUtil.GameSpecific;

public interface AbstractionManager<T extends AbstractionApi> extends MinecraftVersion, SupportedVersions {
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
