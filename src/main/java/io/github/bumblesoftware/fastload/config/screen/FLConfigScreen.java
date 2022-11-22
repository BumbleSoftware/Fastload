package io.github.bumblesoftware.fastload.config.screen;

import io.github.bumblesoftware.fastload.config.modmenu.button.MFModMenuButtons;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static io.github.bumblesoftware.fastload.constants.FLColourConstants.white;

public class FLConfigScreen extends SimpleOptionsScreen {
    private final Screen parent;
    private static final Text title = Text.translatable("fastload.screen.config");
    private static final MinecraftClient client = MinecraftClient.getInstance();
    public FLConfigScreen(Screen parent) {
        super(parent, client.options, title, MFModMenuButtons.asOptions());
        this.parent = parent;
    }
    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, (button) ->
                client.setScreen(this.parent))); // Save Config on setScreen()
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        DrawableHelper.drawCenteredText(
                matrices,
                this.textRenderer,
                title,
                 this.width / 2,
                5,
                white
        );
        super.render(matrices, mouseX, mouseY, delta);
    }
}
