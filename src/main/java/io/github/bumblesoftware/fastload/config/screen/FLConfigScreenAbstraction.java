package io.github.bumblesoftware.fastload.config.screen;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

public interface FLConfigScreenAbstraction {
     Text TITLE = new TranslatableText("fastload.screen.config");
     MinecraftClient CLIENT = MinecraftClient.getInstance();
     default void initFooter(Screen currentScreen, Screen parent) {
         ABSTRACTED_CLIENT.addDrawableChild(currentScreen,
                 ABSTRACTED_CLIENT.getNewButton(
                         currentScreen.width / 2 - 100,
                         currentScreen.height - 27,
                         200, 20,
                         ScreenTexts.DONE,
                         (button) -> {
                             FLConfig.writeToDisk();
                             CLIENT.setScreen(parent);
                         })
            );
    }
}
