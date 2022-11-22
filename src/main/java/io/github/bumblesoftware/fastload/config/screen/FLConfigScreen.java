package io.github.bumblesoftware.fastload.config.screen;

import io.github.bumblesoftware.fastload.config.modmenu.button.MFModMenuButtons;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static io.github.bumblesoftware.fastload.util.FLColourConstants.white;

public class FLConfigScreen extends GameOptionsScreen {
    private ButtonListWidget list;
    private final Screen parent;
    private static final Text title = Text.translatable("fastload.screen.config");
    private final MinecraftClient client = MinecraftClient.getInstance();
    public FLConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, title);
        this.parent = parent;
    }
    @Override
    protected void init() {
        super.init();
        list = new ButtonListWidget(client, width, height, 50, height, 50);
        list.addAll(MFModMenuButtons.asOptions());
        this.addSelectableChild(this.list);
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, (button) ->
                this.client.setScreen(this.parent))); // Save on setScreen()
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        list.render(matrices, mouseX, mouseY, delta);
        DrawableHelper.drawCenteredText(
                matrices,
                this.textRenderer,
                title,
                this.width / 2,
                this.height / 2,
                white
        );
        this.addDrawableChild(list);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
