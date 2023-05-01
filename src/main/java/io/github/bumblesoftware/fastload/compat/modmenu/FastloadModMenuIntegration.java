package io.github.bumblesoftware.fastload.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.bumblesoftware.fastload.api.internal.abstraction.AbstractClientCalls;
import net.minecraft.client.gui.screen.Screen;

import static io.github.bumblesoftware.fastload.init.FastloadClient.MINECRAFT_ABSTRACTION;

public class FastloadModMenuIntegration implements ModMenuApi {
    public static final AbstractClientCalls ABSTRACTED_CLIENT = MINECRAFT_ABSTRACTION.getAbstractedEntries();
    /**
     *  Registers our config screen to modmenu
     */
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return ABSTRACTED_CLIENT::newFastloadConfigScreen;
    }
}
