package io.github.bumblesoftware.fastload.config.screen;

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
}
