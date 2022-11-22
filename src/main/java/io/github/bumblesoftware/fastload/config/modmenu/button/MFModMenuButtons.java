package io.github.bumblesoftware.fastload.config.modmenu.button;

import com.terraformersmc.modmenu.config.option.BooleanConfigOption;
import net.minecraft.client.option.SimpleOption;

import java.util.ArrayList;

public class MFModMenuButtons {
    public static SimpleOption<?>[] asOptions() {
        ArrayList<SimpleOption<?>> options = new ArrayList<>();
        options.add(new BooleanConfigOption("fastload.debug", true).asOption());
        options.add(new BooleanConfigOption("fastload.force_close", true).asOption());
        return options.toArray(SimpleOption<?>[]::new);
    }
}
