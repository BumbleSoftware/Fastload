package io.github.bumblesoftware.fastload.abstraction.client119;

import com.mojang.serialization.Codec;
import io.github.bumblesoftware.fastload.abstraction.client1182.Client1182;
import io.github.bumblesoftware.fastload.abstraction.client1182.RetrieveValueFunction;
import io.github.bumblesoftware.fastload.abstraction.client1182.StoreValueFunction;
import io.github.bumblesoftware.fastload.config.screen.FLConfigScreen119;
import io.github.bumblesoftware.fastload.util.MinMaxHolder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class Client119 extends Client1182 {

    @Override
    public Text newTranslatableText(String content) {
        return Text.translatable(content);
    }

    @Override
    public Text newLiteralText(String content) {
        return Text.literal(content);
    }

    @Override
    public boolean isWindowFocused() {
        return getClientInstance().isWindowFocused();
    }

    @Override
    public Screen newFastloadConfigScreen(Screen parent) {
        return new FLConfigScreen119(parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SimpleOption<Boolean> newCyclingButton(
            String namespace, String identifier, RetrieveValueFunction retrieveValueFunction,
            StoreValueFunction storeValueFunction
    ) {
        return SimpleOption.ofBoolean(
                namespace + identifier,
                SimpleOption.constantTooltip(Text.translatable(namespace + identifier + ".tooltip")),
                Boolean.parseBoolean(retrieveValueFunction.getValue(identifier))
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T newSlider(String namespace, String identifier, MinMaxHolder minMaxValues, RetrieveValueFunction retrieveValueFunction, StoreValueFunction storeValueFunction, int width) {
        int max = minMaxValues.max();
        int min = minMaxValues.min();
        return (T) new SimpleOption<>(
                namespace + identifier,
                SimpleOption.constantTooltip(Text.translatable(namespace + identifier + ".tooltip")),
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
                value -> {}
        );
    }
}
