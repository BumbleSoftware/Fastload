package io.github.bumblesoftware.fastload.abstraction.tool;

import net.minecraft.client.gui.screen.Screen;

public interface ScreenProvider {
    boolean getCurrent(final Screen screen);
}
