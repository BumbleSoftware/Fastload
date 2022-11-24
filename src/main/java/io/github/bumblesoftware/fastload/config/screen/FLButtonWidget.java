package io.github.bumblesoftware.fastload.config.screen;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class FLButtonWidget extends ButtonWidget {
    public FLButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    }
}
