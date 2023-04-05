package io.github.bumblesoftware.fastload.abstraction.client;

import com.mojang.serialization.Codec;
import io.github.bumblesoftware.fastload.abstraction.tool.RetrieveValueFunction;
import io.github.bumblesoftware.fastload.abstraction.tool.StoreValueFunction;
import io.github.bumblesoftware.fastload.config.screen.FLConfigScreen119;
import io.github.bumblesoftware.fastload.util.MinMaxHolder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class Client119 extends Client1182 {
    @Override
    public String getVersion() {
        return "1.19/1.19.1/1.19.2";
    }

    @Override
    public Screen newFastloadConfigScreen(final Screen parent) {
        return new FLConfigScreen119(parent);
    }

    @Override
    public Text newTranslatableText(final String content) {
        return Text.translatable(content);
    }

    @Override
    public Text newLiteralText(final String content) {
        return Text.literal(content);
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    @Override
    public SimpleOption<Integer> newSlider(
            final String namespace,
            final String identifier,
            final RetrieveValueFunction retrieveValueFunction,
            final StoreValueFunction storeValueFunction,
            final MinMaxHolder minMaxValues,
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

    @Override
    public boolean isWindowFocused() {
        return getClientInstance().isWindowFocused();
    }
}
