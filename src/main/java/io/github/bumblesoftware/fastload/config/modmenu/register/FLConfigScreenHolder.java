package io.github.bumblesoftware.fastload.config.modmenu.register;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import io.github.bumblesoftware.fastload.config.screen.FLConfigScreen;
import net.minecraft.client.gui.screen.Screen;

public class FLConfigScreenHolder implements ConfigScreenFactory<Screen> {
    @Override
    public Screen create(Screen parent) {
        return new FLConfigScreen();
    }
}
