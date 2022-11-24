package io.github.bumblesoftware.fastload.config.screen;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.config.modmenu.button.FLModMenuButtons;
import io.github.bumblesoftware.fastload.init.FastLoad;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.function.Supplier;

/**
 * Class extends SimpleOptionsScreen, so that ends up doing most of the work anyway.
 */
@SuppressWarnings("DanglingJavadoc")
public class FLConfigScreen extends SimpleOptionsScreen {
    private final Screen parent;
    private static final Text title = Text.translatable("fastload.screen.config");
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Supplier<SimpleOption<?>[]> array = FLModMenuButtons::asOptions;
    public FLConfigScreen(Screen parent) {
        super(parent, client.options, title, FLModMenuButtons.asOptions());
        this.parent = parent;
    }

    @Override
    protected void initFooter() {
        this.addDrawableChild(new FLButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> {
            /**
             * When the button "Done" is pressed, this for loop simply iterates through the values stored in memory,
             * the addresses to the config & writes it.
             */
            for (int i = 0; i < array.get().length; i++) {
                String key = FLModMenuButtons.getButtonAddresses(i);
                String value = array.get()[i].getValue().toString().toLowerCase();
                if (FLMath.getDebug())
                    FastLoad.LOGGER.info(key.toUpperCase() + ": " + value.toUpperCase());
                FLConfig.writeToDisk(key, value, i >= 4);
            }
            /**
             * Then, once it's written, the screen is closed
             */
            client.setScreen(this.parent);
        }));
    }

    /**
     * Background is opaque
     */
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
