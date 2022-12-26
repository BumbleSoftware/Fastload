package com.abdelaziz.fastload.util;

/**
 * This is a record that stores the "BOUNDS" in com.abdelaziz.fastload.config.init.DefaultConfig
 * Min and Maxes are always stored together because seperating them causes spaghetti code, we don't want that.
 */
public record MinMaxHolder(int max, int min) {
}
