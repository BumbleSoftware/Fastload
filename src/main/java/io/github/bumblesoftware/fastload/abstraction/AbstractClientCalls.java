package io.github.bumblesoftware.fastload.abstraction;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;


/**
 * All client method calls are in this interface to be implemented differently for each version in order to allow
 * compat.
 */
public interface AbstractClientCalls {

    MinecraftClient getClientInstance();
    ClientWorld getClientWorld();
    Screen getFastloadConfigScreen(Screen parent);
    Text getNewTranslatableText(String content);
    int getLoadedChunkCount();
    int getCompletedChunkCount();
    void setScreen(Screen screen);
    void renderScreenBackgroundTexture(Screen screen, int offset);
    void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, int centerX, int y, int color);
    void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, String text, int centerX, int y, int color);
    boolean isWindowFocused();
    boolean isGameMenuScreen(Screen screen);
    boolean isProgressScreen(Screen screen);
    boolean isDownloadingTerrainScreen(Screen screen);
}
