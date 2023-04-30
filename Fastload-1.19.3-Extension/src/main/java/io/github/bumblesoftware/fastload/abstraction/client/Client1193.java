package io.github.bumblesoftware.fastload.abstraction.client;


import com.mojang.serialization.Codec;
import io.github.bumblesoftware.fastload.abstraction.tool.RetrieveValueFunction;
import io.github.bumblesoftware.fastload.abstraction.tool.StoreValueFunction;
import io.github.bumblesoftware.fastload.util.Bound;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class Client1193 extends Client119 {
    @Override
    public String[] getSupportedMinecraftVersions() {
        return new String[] {"1.19.3"};
    }

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

    @Override
    public SimpleOption<Boolean> newCyclingButton(
            final String namespace,
            final String identifier,
            final RetrieveValueFunction retrieveValueFunction,
            final StoreValueFunction storeValueFunction
    ) {
        return SimpleOption.ofBoolean(
                namespace + identifier,
                SimpleOption.constantTooltip(newTranslatableText(namespace + identifier + ".tooltip")),
                Boolean.parseBoolean(retrieveValueFunction.getValue(identifier)),
                aBoolean -> storeValueFunction.setValue(identifier, Boolean.toString(aBoolean))
        );
    }

    @Override
    public SimpleOption<Integer> newSlider(
            final String namespace,
            final String identifier,
            final RetrieveValueFunction retrieveValueFunction,
            final StoreValueFunction storeValueFunction,
            final Bound minMaxValues,
            final int width
    ) {
        int max = minMaxValues.max();
        int min = minMaxValues.min();
        return new SimpleOption<>(
                namespace + identifier,
                SimpleOption.constantTooltip(newTranslatableText(namespace + identifier + ".tooltip")),
                (optionText, value) -> {
                    if (value.equals(min)) {
                        return GameOptions.getGenericValueText(optionText, Text.translatable(namespace + identifier + ".min"));
                    } else if (value.equals(max)) {
                        return GameOptions.getGenericValueText(optionText, Text.translatable(namespace + identifier + ".max"));
                    } else {
                        return GameOptions.getGenericValueText(optionText, value);
                    }
                },
                new SimpleOption.ValidatingIntSliderCallbacks(min, max),
                Codec.DOUBLE.xmap(value -> max, value -> (double) value - max),
                Integer.parseInt(retrieveValueFunction.getValue(identifier)),
                value -> storeValueFunction.setValue(identifier, Integer.toString(value))
        );
    }
}
