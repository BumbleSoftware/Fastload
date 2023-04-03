package io.github.bumblesoftware.fastload.abstraction.client1182.screen;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

/**
 * Initialised Fastload's config screen
 */
public class FLConfigScreen1182 extends SimpleOptionsScreen {
    private static final Text TITLE = new TranslatableText("fastload.screen.config");
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public FLConfigScreen1182(Screen parent) {
        super(parent, CLIENT.options, TITLE, FLConfigScreenButtons1182.asOptions());
    }
    @Override
    protected void initFooter() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, (button) -> {
            FLConfig.writeToDisk();
            CLIENT.setScreen(this.parent);
        }));
    }
}

