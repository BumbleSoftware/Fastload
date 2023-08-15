package io.github.bumblesoftware.fastload.abstraction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * All client method calls are in this interface to be implemented differently for each version in order to allow
 * compat.
 */
@SuppressWarnings({"UnusedReturnValue", "BooleanMethodIsAlwaysInverted"})
public interface AbstractClientCalls {

    Minecraft getClientInstance();
    @SuppressWarnings("unused")
    Component newLiteralText(final String content);
    void setScreen(final Screen screen);
}