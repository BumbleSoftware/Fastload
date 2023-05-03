package io.github.bumblesoftware.fastload.api.internal.abstraction;

import net.minecraft.client.gui.screen.Screen;

public interface ScreenProvider {
    boolean getCurrent(final Screen screen);
}
