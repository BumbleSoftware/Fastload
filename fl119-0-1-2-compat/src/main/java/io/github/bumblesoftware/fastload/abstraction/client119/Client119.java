package io.github.bumblesoftware.fastload.abstraction.client119;

import io.github.bumblesoftware.fastload.abstraction.client1182.Client1182;
import net.minecraft.text.Text;

public class Client119 extends Client1182 {

    @Override
    public Text newTranslatableText(String content) {
        return Text.translatable(content);
    }

    @Override
    public boolean isWindowFocused() {
        return getClientInstance().isWindowFocused();
    }
}
