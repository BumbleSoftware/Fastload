package io.github.bumblesoftware.fastload.compat.modmenu;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.text.Text;

import static io.github.bumblesoftware.fastload.config.FLConfig.writeToDisk;
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

public interface FLConfigScreenAbstraction {
     Text TITLE = ABSTRACTED_CLIENT.newTranslatableText("fastload.screen.config");
     MinecraftClient CLIENT = MinecraftClient.getInstance();
     @SuppressWarnings("unused")
     default void initFooter(Screen currentScreen, Screen parent) {
         ABSTRACTED_CLIENT.addDrawableChild(currentScreen,
                 ABSTRACTED_CLIENT.getNewButton(
                         currentScreen.width / 2 - 100,
                         currentScreen.height - 27,
                         200, 20,
                         ScreenTexts.DONE,
                         (button) -> {
                             storeValues();
                             writeToDisk();
                             CLIENT.setScreen(parent);
                         })
            );
    }

    default void storeValues() {}
}
