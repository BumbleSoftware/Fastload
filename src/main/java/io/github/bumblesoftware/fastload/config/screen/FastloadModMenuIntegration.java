package io.github.bumblesoftware.fastload.config.screen;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

public class FastloadModMenuIntegration implements ModMenuApi {
    /**
     *  Registers our config screen to modmenu
     */
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return ABSTRACTED_CLIENT::newFastloadConfigScreen;
    }
}
