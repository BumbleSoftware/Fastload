package io.github.bumblesoftware.fastload.config.modmenu.register;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

public class FLModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return new FLConfigScreenHolder();
    }
}
