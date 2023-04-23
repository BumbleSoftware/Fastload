package io.github.bumblesoftware.fastload.version;

import io.github.bumblesoftware.fastload.init.Fastload;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.BiFunction;

import static io.github.bumblesoftware.fastload.config.FLMath.isDebugEnabled;
import static io.github.bumblesoftware.fastload.version.VersionHelper.MatchingStrategy.EQUALS;

public class VersionHelper {
    public static boolean matchesAny(
            final MatchingStrategy strategy,
            final boolean doAll,
            final String... comparedVersions
    ) {
        boolean storedBoolean = false;
        for (String comparedVersion : comparedVersions) {
            final boolean matches = strategy.function.apply(getVersion(), comparedVersion);
            if (isDebugEnabled())
                Fastload.LOGGER.info(
                        "VERSIONS:[" + getVersion() + ", " + comparedVersion + "], MATCHES: " + matches
                                + ", STRATEGY: " + strategy.name()
                );

           if (matches) {
               if (doAll)
                   storedBoolean = true;
               else return true;
           }
        }
        return storedBoolean;
    }

    public static boolean matchesAny(
            final MatchingStrategy strategy,
            final String... comparedVersions
    ) {
        return matchesAny(strategy, false, comparedVersions);
    }

    public static boolean matchesAny(
            final boolean doAll,
            final String... comparedVersions
    ) {
        return matchesAny(EQUALS, doAll, comparedVersions);
    }


    public static boolean matchesAny(final String... comparedVersions) {
        return matchesAny(EQUALS, false, comparedVersions);
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

    public enum MatchingStrategy {
        EQUALS(String::equals),
        REGEX(VersionHelper::regexMatch);

        public final BiFunction<String, String, Boolean> function;
        MatchingStrategy(final BiFunction<String, String, Boolean> function) {
            this.function = function;
        }
    }
}
