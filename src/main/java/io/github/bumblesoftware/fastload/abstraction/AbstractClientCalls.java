package io.github.bumblesoftware.fastload.abstraction;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;


/**
 * All client method calls are in this interface to be implemented differently for each version in order to allow
 * compat.
 */
public interface AbstractClientCalls {

    MinecraftClient getClientInstance();
    Camera getCamera();
    ClientWorld getClientWorld();
    Screen getFastloadConfigScreen(Screen parent);
    Text getNewTranslatableText(String content);
    int getLoadedChunkCount();
    int getCompletedChunkCount();
    float getPlayerYaw();
    float getPlayerPitch();
    void setPlayerPitch(float pitch);
    void setPlayerRotation(float yaw, float pitch);
    void setScreen(Screen screen);
    void renderScreenBackgroundTexture(Screen screen, int offset);
    void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, int centerX, int y, int color);
    void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, String text, int centerX, int y, int color);
    boolean isWindowFocused();
    boolean isGameMenuScreen(Screen screen);
    boolean isProgressScreen(Screen screen);
    boolean isDownloadingTerrainScreen(Screen screen);
}
