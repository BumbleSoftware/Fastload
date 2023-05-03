package io.github.bumblesoftware.fastload.abstraction;

import net.minecraft.client.gui.screen.Screen;

public interface ScreenProvider {
    boolean getCurrent(final Screen screen);
}
