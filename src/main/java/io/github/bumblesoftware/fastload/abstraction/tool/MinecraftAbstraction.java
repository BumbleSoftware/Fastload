package io.github.bumblesoftware.fastload.abstraction.tool;

import io.github.bumblesoftware.fastload.version.VersionUtil.GameSpecific;

public interface MinecraftAbstraction<T extends SupportedVersions> extends MinecraftVersion, SupportedVersions {
    @Override
    default String getMinecraftVersion() {
        return getMinecraftVersionUtil().providedVersion;
    }

    @Override
    default String[] getSupportedMinecraftVersions() {
        return getAbstractedEntries().getSupportedMinecraftVersions();
    }

    GameSpecific getMinecraftVersionUtil();

    T getAbstractedEntries();
}
