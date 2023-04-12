package io.github.bumblesoftware.fastload.abstraction.client;


import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class Client1194 extends Client1193 {
    @Override
    public String getCompatibleVersions() {
        return "1.19.4";
    }

    @Override
    public void renderScreenBackgroundTexture(
            final Screen screen,
            final int offset,
            final MatrixStack matrices
    ) {
        screen.renderBackgroundTexture(matrices);
    }

    @Override
    public void drawCenteredText(
            final MatrixStack matrices,
            final TextRenderer textRenderer,
            final Text text,
            final int centerX,
            final int y,
            final int color
    ) {
        DrawableHelper.drawCenteredTextWithShadow(matrices, textRenderer, text, centerX, y, color);
    }

    @Override
    public void drawCenteredText(
            final MatrixStack matrices,
            final TextRenderer textRenderer,
            final String text,
            final int centerX,
            final int y,
            final int color
    ) {
        DrawableHelper.drawCenteredTextWithShadow(matrices, textRenderer, text, centerX, y, color);
    }
}
