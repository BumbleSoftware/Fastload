package io.github.bumblesoftware.fastload.abstraction;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@SuppressWarnings("unchecked")
public class Client1193 implements AbstractClientCalls {

    @Override
    public MinecraftClient getClientInstance() {
        return MinecraftClient.getInstance();
    }

    @Override
    public Text newLiteralText(final String content) {
        return Text.literal(content);
    }

    @Override
    public void setScreen(final Screen screen) {
        getClientInstance().setScreen(screen);
    }
}