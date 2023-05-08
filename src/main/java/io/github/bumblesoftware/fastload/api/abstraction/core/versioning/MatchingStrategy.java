package io.github.bumblesoftware.fastload.api.abstraction.core.versioning;

import java.util.function.BiFunction;

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