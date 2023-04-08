package io.github.bumblesoftware.fastload.abstraction.client;

import io.github.bumblesoftware.fastload.abstraction.tool.RetrieveValueFunction;
import io.github.bumblesoftware.fastload.abstraction.tool.ScreenProvider;
import io.github.bumblesoftware.fastload.abstraction.tool.StoreValueFunction;
import io.github.bumblesoftware.fastload.compat.modmenu.FLConfigScreenButtons;
import io.github.bumblesoftware.fastload.util.Bound;
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
@SuppressWarnings({"UnusedReturnValue", "BooleanMethodIsAlwaysInverted"})
public interface AbstractClientCalls {
    default String getCompatibleVersions() {
        return "null";
    }

    MinecraftClient getClientInstance();
    ClientWorld getClientWorld();
    Screen newFastloadConfigScreen(final Screen parent);
    Screen newBuildingTerrainScreen();
    Screen newBuildingTerrainScreen(final int loadingAreaGoal);

    Screen getCurrentScreen();
    Text newTranslatableText(final String content);
    Text newLiteralText(final String content);
    <T extends Element & Drawable> T addDrawableChild(final Screen screen, final T drawableElement);
    <T> FLConfigScreenButtons<T> newFLConfigScreenButtons();
    ButtonWidget getNewButton(
            final int x,
            final int y,
            final int width,
            final int height,
            final Text message,
            final ButtonWidget.PressAction onPress
    );
    <T> T newCyclingButton(
            final String namespace,
            final String identifier,
            final RetrieveValueFunction retrieveValueFunction,
            final StoreValueFunction storeValueFunction
    );
    <T> T newSlider(
            final String namespace,
            final String identifier,
            final RetrieveValueFunction retrieveValueFunction,
            final StoreValueFunction storeValueFunction,
            final Bound minMaxValues,
            final int width
    );


    void setScreen(final Screen screen);
    void renderScreenBackgroundTexture(
            final Screen screen,
            final int offset,
            final MatrixStack matrices
    );
    void drawCenteredText(
            final MatrixStack matrices,
            final TextRenderer textRenderer,
            final Text text,
            final int centerX,
            final int y,
            final int color
    );
    void drawCenteredText(
            final MatrixStack matrices,
            final TextRenderer textRenderer,
            final String text,
            final int centerX,
            final int y,
            final int color
    );

    int getLoadedChunkCount();
    int getCompletedChunkCount();
    int getViewDistance();

    boolean isWindowFocused();
    boolean isSingleplayer();
    boolean forCurrentScreen(final ScreenProvider screenProvider);
    boolean isBuildingTerrainScreen(final Screen screen);
    boolean isGameMenuScreen(final Screen screen);
    boolean isProgressScreen(final Screen screen);
    boolean isDownloadingTerrainScreen(final Screen screen);
}
