package io.github.bumblesoftware.fastload.api.abstraction.core.versioning;

import java.util.function.Function;

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