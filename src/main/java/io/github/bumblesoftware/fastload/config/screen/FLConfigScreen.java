package io.github.bumblesoftware.fastload.config.screen;

import io.github.bumblesoftware.fastload.config.modmenu.button.MFModMenuButtons;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class FLConfigScreen extends Screen {
    private final ButtonListWidget list;
    public FLConfigScreen() {
        super(Text.translatable("fastload.screen.config"));
        list = new ButtonListWidget(MinecraftClient.getInstance(), this.width, this.height, 32, this.height - 32, 25);
        list.addAll(MFModMenuButtons.asOptions());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        list.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
