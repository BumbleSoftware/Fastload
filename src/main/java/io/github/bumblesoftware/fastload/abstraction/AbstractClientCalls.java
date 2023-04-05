package io.github.bumblesoftware.fastload.abstraction;

import io.github.bumblesoftware.fastload.abstraction.client1182.RetrieveValueFunction;
import io.github.bumblesoftware.fastload.abstraction.client1182.StoreValueFunction;
import io.github.bumblesoftware.fastload.config.screen.FLConfigScreenButtons;
import io.github.bumblesoftware.fastload.util.MinMaxHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
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
    Screen newFastloadConfigScreen(Screen parent);
    Screen newBuildingTerrainScreen();
    Text newTranslatableText(String content);
    Text newLiteralText(String content);
    int getLoadedChunkCount();
    int getCompletedChunkCount();
    void setScreen(Screen screen);
    <T> FLConfigScreenButtons<T> newFLConfigScreenButtons();
    @SuppressWarnings("UnusedReturnValue")
    <T extends Element & Drawable> T addDrawableChild(Screen screen, T drawableElement);
    ButtonWidget getNewButton(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress);
    <T> T newCyclingButton(
            String namespace, String identifier, RetrieveValueFunction retrieveValueFunction,
            StoreValueFunction storeValueFunction);
    <T> T newSlider(String namespace, String identifier, MinMaxHolder minMaxValues,
                    RetrieveValueFunction retrieveValueFunction, StoreValueFunction storeValueFunction, int width);
    void renderScreenBackgroundTexture(Screen screen, int offset, MatrixStack matrices);
    void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, int centerX, int y, int color);
    void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, String text, int centerX, int y, int color);
    boolean isWindowFocused();
    boolean isGameMenuScreen(Screen screen);
    boolean isProgressScreen(Screen screen);
    boolean isDownloadingTerrainScreen(Screen screen);
}
