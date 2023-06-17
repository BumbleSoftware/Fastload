package io.github.bumblesoftware.fastload.abstraction;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;


/**
 * All client method calls are in this interface to be implemented differently for each version in order to allow
 * compat.
 */
@SuppressWarnings({"UnusedReturnValue", "BooleanMethodIsAlwaysInverted"})
public interface AbstractClientCalls {

    MinecraftClient getClientInstance();
    @SuppressWarnings("unused")
    Text newLiteralText(final String content);
    void setScreen(final Screen screen);
}