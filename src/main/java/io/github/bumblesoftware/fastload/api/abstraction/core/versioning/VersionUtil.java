package io.github.bumblesoftware.fastload.api.abstraction.core.versioning;

import java.util.function.Function;


public class VersionUtil {
    public final String gameId;
    public final String providedVersion;
    public final MatchingStrategy defaultMatchingStrategy;
    public final ExceptionStrategy defaultException;

    public VersionUtil(
            final String gameId,
            final Function<String ,VersionPackage> providedVersion,
            final MatchingStrategy defaultMatchingStrategy,
            final ExceptionStrategy defaultException
    ) {
        this.gameId = gameId;
        this.providedVersion = providedVersion.apply(gameId).getVersion();
        this.defaultMatchingStrategy = defaultMatchingStrategy;
        this.defaultException = defaultException;

    }

    @Override
    public String toString() {
        return "VersionHelper.GameSpecific[id=" + gameId +",version= " + providedVersion +"]" + hashCode();
    }

    public boolean matchesAny(
            final String comparedVersion,
            final MatchingStrategy strategy,
            final ExceptionStrategy checkForExceptions
    ) {
        return strategy.function.apply(providedVersion, comparedVersion) && checkForExceptions.function.apply(providedVersion);
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

    @SuppressWarnings("unused")
    public boolean matchesAny(
            final String comparedVersion
    ) {
        return matchesAny(comparedVersion, defaultMatchingStrategy, defaultException);
    }
}
