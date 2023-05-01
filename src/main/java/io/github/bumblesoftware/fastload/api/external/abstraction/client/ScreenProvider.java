package io.github.bumblesoftware.fastload.api.external.abstraction.client;

import net.minecraft.client.gui.screen.Screen;

public interface ScreenProvider {
    boolean getCurrent(final Screen screen);
}
