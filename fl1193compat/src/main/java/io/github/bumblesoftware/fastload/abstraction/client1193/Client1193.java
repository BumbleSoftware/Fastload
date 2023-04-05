package io.github.bumblesoftware.fastload.abstraction.client1193;


import io.github.bumblesoftware.fastload.abstraction.client119.Client119;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class Client1193 extends Client119 {
    @Override
    public ButtonWidget getNewButton(
            final int x,
            final int y,
            final int width,
            final int height,
            final Text message,
            final ButtonWidget.PressAction onPress
    ) {
        return ButtonWidget.builder(message, onPress)
                .dimensions(x, y, width, height)
                .build();
    }
}
