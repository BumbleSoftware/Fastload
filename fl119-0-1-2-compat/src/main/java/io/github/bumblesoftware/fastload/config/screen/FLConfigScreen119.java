package io.github.bumblesoftware.fastload.config.screen;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.option.SimpleOption;

import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

public class FLConfigScreen119 extends SimpleOptionsScreen implements FLConfigScreenAbstraction {
    public FLConfigScreen119(Screen parent) {
        super(parent, CLIENT.options, TITLE,
                (SimpleOption<?>[]) ABSTRACTED_CLIENT.newFLConfigScreenButtons()
                        .getAllOptions(new SimpleOption<?>[]{}));
    }

    @Override
    public void storeValues() {
        for (SimpleOption<?> option : options) {
            String key = option.toString();
            String value = option.getValue().toString().toLowerCase();
            if (FLMath.isDebugEnabled())
                Fastload.LOGGER.info(key.toUpperCase() + ": " + value.toUpperCase());
            FLConfig.storeProperty(key, value);
        }
    }
}
