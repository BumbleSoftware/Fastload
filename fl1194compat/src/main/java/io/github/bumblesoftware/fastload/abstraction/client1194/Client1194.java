package io.github.bumblesoftware.fastload.abstraction.client1194;


import io.github.bumblesoftware.fastload.abstraction.client1193.Client1193;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static net.minecraft.client.gui.DrawableHelper.drawCenteredTextWithShadow;

public class Client1194 extends Client1193 {
    @Override
    public void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, int centerX, int y, int color) {
        drawCenteredTextWithShadow(matrices, textRenderer, text, centerX, y, color);
    }

    @Override
    public void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, String text, int centerX, int y, int color) {
        drawCenteredTextWithShadow(matrices, textRenderer, text, centerX, y, color);
    }
}
