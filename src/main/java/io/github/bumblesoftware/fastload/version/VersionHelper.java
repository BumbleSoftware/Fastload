package io.github.bumblesoftware.fastload.version;

import io.github.bumblesoftware.fastload.init.Fastload;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.github.bumblesoftware.fastload.config.FLMath.isDebugEnabled;
import static io.github.bumblesoftware.fastload.version.VersionHelper.ExceptionStrategy.*;
import static io.github.bumblesoftware.fastload.version.VersionHelper.MatchingStrategy.EQUALS;
import static io.github.bumblesoftware.fastload.version.VersionHelper.VersionPackage.*;

public class VersionHelper {
    public static final GameSpecific MINECRAFT = new GameSpecific(ofFmjVersion("minecraft"), EQUALS, NO_EXCEPTION);


    public static class GameSpecific {
        public final String providedVersion;
        public final MatchingStrategy defaultMatchingStrategy;
        public final ExceptionStrategy defaultException;

        GameSpecific(
                final VersionPackage providedVersion,
                final MatchingStrategy defaultMatchingStrategy,
                final ExceptionStrategy defaultException
        ) {
            this.providedVersion = providedVersion.getVersion();
            this.defaultMatchingStrategy = defaultMatchingStrategy;
            this.defaultException = defaultException;

        }

        public boolean matchesAny(
                final String comparedVersion,
                final MatchingStrategy strategy,
                final ExceptionStrategy checkForExceptions
        ) {
            final boolean matches =
                    strategy.function.apply(providedVersion, comparedVersion) && checkForExceptions.function.apply(providedVersion);
            if (isDebugEnabled())
                Fastload.LOGGER.info(
                        "VERSIONS:[" + providedVersion + ", " + comparedVersion + "], MATCHES: " + matches
                                + ", STRATEGY: " + strategy.name()
                );
            return matches;
        }

        public boolean matchesAny(
                final String comparedVersion,
                final MatchingStrategy strategy
        ) {
            return matchesAny(comparedVersion, strategy, defaultException);
        }

        @SuppressWarnings("unused")
        public boolean matchesAny(
                final String comparedVersion,
                final ExceptionStrategy checkForExceptions
        ) {
            return matchesAny(comparedVersion, defaultMatchingStrategy, checkForExceptions);
        }

        public boolean matchesAny(
                final String comparedVersion
        ) {
            return matchesAny(comparedVersion, defaultMatchingStrategy, defaultException);
        }
    }

    public enum ExceptionStrategy {
        NO_EXCEPTION(version -> true),
        ONLY_FIRST_MAJOR(version -> {
            final var array = version.toCharArray();
            return !(array.length >= 6 && array[4] == '.' && Character.isDigit(array[5]));
        });

        public final Function<String, Boolean> function;
        ExceptionStrategy(final Function<String, Boolean> function) {
            this.function = function;
        }
    }

    public enum MatchingStrategy {
        EQUALS(String::equals),
        REGEX(MatchingStrategy::regexMatch);

        public final BiFunction<String, String, Boolean> function;
        MatchingStrategy(final BiFunction<String, String, Boolean> function) {
            this.function = function;
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
    }

    public static class VersionPackage {
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
}
