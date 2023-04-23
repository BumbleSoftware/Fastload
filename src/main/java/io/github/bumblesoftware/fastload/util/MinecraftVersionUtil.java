package io.github.bumblesoftware.fastload.util;

import io.github.bumblesoftware.fastload.init.Fastload;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.BiFunction;

import static io.github.bumblesoftware.fastload.config.FLMath.isDebugEnabled;

public class MinecraftVersionUtil {
    public static final BiFunction<String, String, Boolean> EQUALS = String::equals;
    public static final BiFunction<String, String, Boolean> REGEX = MinecraftVersionUtil::regexMatch;

    public static boolean matchesAny(
            final BiFunction<String, String, Boolean> strategy,
            final String... comparedVersions
    ) {
        for (String comparedVersion : comparedVersions) {
            boolean matches = strategy.apply(getVersion(), comparedVersion);
            if (isDebugEnabled())
                Fastload.LOGGER.info(
                        "VERSIONS:[" + getVersion() + ", " + comparedVersion + "], MATCHES: " + matches
                );
            if (matches)
                return true;
        }
        return false;
    }

    public static boolean regexMatch(final String main, final String regex) {
        int matchesInARow = 0;
        final char[] regexChars = regex.toCharArray();
        final char[] mainChars = main.toCharArray();
        for (char mainChar : mainChars) {
            if (regexChars[matchesInARow] == mainChar)
                matchesInARow++;
            else matchesInARow = 0;
            if (matchesInARow == regexChars.length)
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
