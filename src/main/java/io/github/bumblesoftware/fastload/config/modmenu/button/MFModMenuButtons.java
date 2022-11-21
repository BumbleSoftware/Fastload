package io.github.bumblesoftware.fastload.config.modmenu.button;

import com.terraformersmc.modmenu.config.option.BooleanConfigOption;
import net.minecraft.client.option.SimpleOption;

import java.util.ArrayList;

public class MFModMenuButtons {

    public static final BooleanConfigOption TEST;
    public static final SimpleOption<?> BOOLEAN;

    static {
        TEST = new BooleanConfigOption("test", true);
        BOOLEAN = SimpleOption.ofBoolean("boolean", false);
    }
    public static SimpleOption<?>[] asOptions() {
        ArrayList<SimpleOption<?>> options = new ArrayList<>();
        options.add(BOOLEAN);
        return options.toArray(SimpleOption<?>[]::new);
    }
}
