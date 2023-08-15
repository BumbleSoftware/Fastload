package io.github.bumblesoftware.fastload.abstraction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@SuppressWarnings("unchecked")
public class Client1193 implements AbstractClientCalls {
    @Override
    public Minecraft getClientInstance() {
        return Minecraft.getInstance();
    }

    @Override
    public Component newLiteralText(final String content) {
        return Component.literal(content);
    }

    @Override
    public void setScreen(final Screen screen) {
        getClientInstance().setScreen(screen);
    }
}