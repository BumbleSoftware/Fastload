package io.github.bumblesoftware.fastload.api.abstraction.core.versioning;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public class VersionPackage {
    private final String info;
    private final Function<String, String> versionGetter;

    private VersionPackage(
            final String name,
            final @Nullable Function<String, String> versionGetter
    ) {
        this.info = name;
        this.versionGetter = Objects.requireNonNullElseGet(versionGetter, () -> string -> string);
    }

    public String getVersion() {
        return versionGetter.apply(info);
    }

    public static VersionPackage of(
            final String info,
            final @Nullable Function<String, String> versionGetter
    ) {
        return new VersionPackage(info, versionGetter);
    }

    @SuppressWarnings("unused")
    public static VersionPackage ofVersion(final String version) {
        return of(version, null);
    }

    public static VersionPackage ofFmjVersion(final String name) {
        return of(name, VersionPackage::getFmjVersion);
    }

    public static String getFmjVersion(final String name) {
        return FabricLoader
                .getInstance()
                .getModContainer(name)
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();
    }
}