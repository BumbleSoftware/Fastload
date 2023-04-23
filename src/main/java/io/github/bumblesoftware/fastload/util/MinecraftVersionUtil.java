package io.github.bumblesoftware.fastload.util;

import io.github.bumblesoftware.fastload.init.Fastload;
import net.fabricmc.loader.api.FabricLoader;

import static io.github.bumblesoftware.fastload.config.FLMath.isDebugEnabled;

public class MinecraftVersionUtil {
    public static boolean matchesAny(final String... versions) {
        for (String version : versions) {
            final boolean matches = getVersion().matches(version);
            if (isDebugEnabled())
                Fastload.LOGGER.info(
                        "VERSIONS:[" + getVersion() + ", " + version + "], MATCHES: " + matches
                );
            if (matches)
                return true;
        }
        return false;
    }

    public static String getVersion() {
        return FabricLoader
                .getInstance()
                .getModContainer("minecraft")
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();
    }
}
