package io.github.bumblesoftware.fastload.abstraction.client.mc119;

import com.mojang.serialization.Codec;
import io.github.bumblesoftware.fastload.api.internal.abstraction.Client1182;
import io.github.bumblesoftware.fastload.api.external.abstraction.core.config.RetrieveValueFunction;
import io.github.bumblesoftware.fastload.api.external.abstraction.core.config.StoreValueFunction;
import io.github.bumblesoftware.fastload.config.DefaultConfig;
import io.github.bumblesoftware.fastload.util.Action;
import io.github.bumblesoftware.fastload.util.Bound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.function.Function;

public class Client119 extends Client1182 {
    @Override
    public String[] getSupportedVersions() {
        return new String[] {"1.19", "1.19.1", "1.19.2"};
    }

    @Override
    public int getViewDistance() {
        if (getClientInstance().options == null) {
            return DefaultConfig.LOCAL_CHUNK_RADIUS_BOUND.max();
        } else return getClientInstance().options.getClampedViewDistance();
    }

    @Override
    public <T> Screen newConfigScreen(
            final Screen parent,
            GameOptions gameOptions,
            final Text title,
            Function<Object[], T[]> options,
            Action config
    ) {
        final var main = this;
        return new SimpleOptionsScreen(
                parent,
                gameOptions,
                title,
                (SimpleOption<?>[]) options.apply(new SimpleOption[]{})
        ) {
            @Override
            protected void initFooter() {
                main.initFooter(this, parent, config);
            }
        };
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
                        return GameOptions.getGenericValueText(optionText,
                                Text.translatable(namespace + identifier + ".max"));
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
