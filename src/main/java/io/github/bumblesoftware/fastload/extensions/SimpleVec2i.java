package io.github.bumblesoftware.fastload.extensions;

/**
 * This is a record that stores the "BOUNDS" in io.github.bumblesoftware.fastload.config.init.DefaultConfig
 * Min and Maxes are always stored together because seperating them causes spaghetti code, we don't want that
 * Vec2 isn't used because it has other unwanted stuff, better to just have our own basic one!
 * Has Vec2 in its name to fit conventions better.
 */
public record SimpleVec2i(int max, int min) {
}
