package io.github.bumblesoftware.fastload.config.screen;

import io.github.bumblesoftware.fastload.config.modmenu.button.FLModMenuButtons;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class FLConfigScreen extends SimpleOptionsScreen {
    private static final Text title = new TranslatableText("fastload.screen.config");
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public FLConfigScreen(Screen parent) {
        super(parent, client.options, title, FLModMenuButtons.asOptions());
    }
}
