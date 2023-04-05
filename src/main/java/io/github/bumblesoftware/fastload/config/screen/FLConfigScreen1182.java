package io.github.bumblesoftware.fastload.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.option.Option;

import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

public class FLConfigScreen1182 extends SimpleOptionsScreen implements FLConfigScreenAbstraction {
    public FLConfigScreen1182(Screen parent) {
        super(parent, CLIENT.options, TITLE,
                (Option[]) ABSTRACTED_CLIENT.newFLConfigScreenButtons().getAllOptions(new Option[]{}));
    }
}

