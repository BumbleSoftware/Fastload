package io.github.bumblesoftware.fastload.abstraction.client1182;

import io.github.bumblesoftware.fastload.abstraction.AbstractClientCalls;
import io.github.bumblesoftware.fastload.abstraction.client1182.screen.FLConfigScreen1182;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class Client1182 implements AbstractClientCalls {
    @Override
    public MinecraftClient getClientInstance() {
        return MinecraftClient.getInstance();
    }

    @Override
    public ClientWorld getClientWorld() {
        return getClientInstance().world;
    }

    @Override
    public Screen getFastloadConfigScreen(Screen parent) {
        return new FLConfigScreen1182(parent);
    }

    @Override
    public Text getNewTranslatableText(String content) {
        return new TranslatableText(content);
    }

    @Override
    public int getLoadedChunkCount() {
        return getClientWorld().getChunkManager().getLoadedChunkCount();
    }

    @Override
    public int getCompletedChunkCount() {
        return getClientInstance().worldRenderer.getCompletedChunkCount();
    }

    @Override
    public void setScreen(Screen screen) {
        getClientInstance().setScreen(screen);
    }

    @Override
    public ButtonWidget getNewButton(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress) {
        return new ButtonWidget(x, y, width, height, message, onPress);
    }

    @Override
    public void renderScreenBackgroundTexture(Screen screen, int offset) {
        screen.renderBackgroundTexture(0);
    }

    @Override
    public void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, int centerX, int y, int color) {
        DrawableHelper.drawCenteredText(matrices, textRenderer, text, centerX, y, color);
    }

    @Override
    public void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, String text, int centerX, int y, int color) {
        DrawableHelper.drawCenteredText(matrices, textRenderer, text, centerX, y, color);
    }

    @Override
    public boolean isWindowFocused() {
        return getClientInstance().isWindowFocused();
    }

    @Override
    public boolean isGameMenuScreen(Screen screen) {
        return screen instanceof GameMenuScreen;
    }

    @Override
    public boolean isProgressScreen(Screen screen) {
        return screen instanceof ProgressScreen;
    }

    @Override
    public boolean isDownloadingTerrainScreen(Screen screen) {
        return screen instanceof DownloadingTerrainScreen;
    }
}
