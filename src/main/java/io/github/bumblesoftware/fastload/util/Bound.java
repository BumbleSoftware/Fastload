package io.github.bumblesoftware.fastload.util;

import java.util.function.Function;

/**
 * This is a record that stores the "BOUNDS" in io.github.bumblesoftware.fastload.config.init.DefaultConfig
 * Min and Maxes are always stored together because seperating them causes spaghetti code, we don't want that.
 */
public record Bound(int max, int min) {
    public static Bound of(final int max, final int min) {
        return new Bound(max, min);
    }

    public static int minMax(final int toCompare, final int max, final int min) {
        return Math.max(Math.min(toCompare, max), min);
    }

    public int minMax(final int toCompare, Function<Integer, Integer> max, Function<Integer, Integer> min) {
        return minMax(toCompare, max.apply(this.max), min.apply(this.min));
    }

    public int minMax(final int toCompare) {
        return minMax(toCompare, max, min);
    }

    public int getRange() {
        return max - min;
    }

    public Bound getNew(final int max, final int min) {
        return of(max, min);
    }
}
