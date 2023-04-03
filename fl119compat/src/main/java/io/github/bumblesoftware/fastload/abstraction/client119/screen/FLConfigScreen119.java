package io.github.bumblesoftware.fastload.abstraction.client119.screen;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.function.Supplier;

import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

/**
 * Class extends SimpleOptionsScreen, so that ends up doing most of the work anyway.
 */
@SuppressWarnings("DanglingJavadoc")
public class FLConfigScreen119 extends SimpleOptionsScreen {
    private static final Text TITLE = Text.translatable("fastload.screen.config");
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final Supplier<SimpleOption<?>[]> ALL_BUTTONS = FLConfigScreenButtons119::asOptions;
    public FLConfigScreen119(Screen parent) {
        super(parent, CLIENT.options, TITLE,FLConfigScreenButtons119.asOptions());
    }

    @Override
    protected void initFooter() {
        this.addDrawableChild(ABSTRACTED_CLIENT.getNewButton(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE,
                (button) -> {
                    /**
                     * When the button "Done" is pressed, this for loop simply iterates through the values stored in memory,
                     * the addresses to the config & writes it.
                     */
                    for (int i = 0; i < ALL_BUTTONS.get().length; i++) {
                        String key = FLConfigScreenButtons119.getButtonAddresses(i);
                        String value = ALL_BUTTONS.get()[i].getValue().toString().toLowerCase();
                        if (FLMath.isDebugEnabled())
                            Fastload.LOGGER.info(key.toUpperCase() + ": " + value.toUpperCase());
                        FLConfig.storeProperty(key, value);
                    }
                    FLConfig.writeToDisk();
                    /**
                     * Then, once it's written, the screen is closed
                     */
                    CLIENT.setScreen(this.parent);
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
