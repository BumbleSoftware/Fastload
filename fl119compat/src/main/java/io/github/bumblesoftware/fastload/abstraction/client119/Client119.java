package io.github.bumblesoftware.fastload.abstraction.client119;

import io.github.bumblesoftware.fastload.abstraction.client1182.Client1182;
import io.github.bumblesoftware.fastload.abstraction.client119.screen.FLConfigScreen119;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class Client119 extends Client1182 {
    @Override
    public Text getNewTranslatableText(String content) {
        return Text.translatable(content);
    }

    @Override
    public Screen getFastloadConfigScreen(Screen parent) {
        return new FLConfigScreen119(parent);
    }
}
