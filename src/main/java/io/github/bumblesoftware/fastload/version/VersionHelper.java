package io.github.bumblesoftware.fastload.version;

import io.github.bumblesoftware.fastload.init.Fastload;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static io.github.bumblesoftware.fastload.config.FLMath.isDebugEnabled;
import static io.github.bumblesoftware.fastload.version.VersionHelper.MatchingStrategy.EQUALS;

public class VersionHelper {
    public static final Supplier<Boolean> NO_VERSION_EXCEPTION = () -> true;
    public static final Supplier<Boolean> ONLY_FIRST_MAJOR = () -> {
        final var array = getMinecraftVersion().toCharArray();
        return !(array.length >= 6 && array[4] == '.' && Character.isDigit(array[5]));
    };

    public static boolean matchesAny(
            final String comparedVersions,
            final MatchingStrategy strategy,
            final Supplier<Boolean> notIllegalEntry
    ) {
        final boolean matches = strategy.function.apply(getMinecraftVersion(), comparedVersions) && notIllegalEntry.get();
        if (isDebugEnabled())
            Fastload.LOGGER.info(
                    "VERSIONS:[" + getMinecraftVersion() + ", " + comparedVersions + "], MATCHES: " + matches
                            + ", STRATEGY: " + strategy.name()
            );
        return matches;
    }

    public static boolean matchesAny(
            final String comparedVersions,
            final MatchingStrategy strategy
    ) {
        return matchesAny(comparedVersions, strategy, NO_VERSION_EXCEPTION);
    }

    public static boolean matchesAny(
            final String comparedVersions
    ) {
        return matchesAny(comparedVersions, EQUALS);
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

    public static String getMinecraftVersion() {
        return FabricLoader
                .getInstance()
                .getModContainer("minecraft")
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();
    }

    public enum MatchingStrategy {
        EQUALS(String::equals),
        REGEX(VersionHelper::regexMatch);

        public final BiFunction<String, String, Boolean> function;
        MatchingStrategy(final BiFunction<String, String, Boolean> function) {
            this.function = function;
        }
    }
}
