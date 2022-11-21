package io.github.bumblesoftware.fastload.config.modmenu.button;

import net.minecraft.client.option.SimpleOption;

import java.util.ArrayList;

public class MFModMenuButtons {


    public static SimpleOption<?>[] asOptions() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        ArrayList<SimpleOption<?>> options = new ArrayList<>();
        //Add stuff here via options.add();
        return options.toArray(SimpleOption<?>[]::new);
    }
}
