package io.github.bumblesoftware.fastload.abstraction.client;

import com.mojang.serialization.Codec;
import io.github.bumblesoftware.fastload.abstraction.tool.RetrieveValueFunction;
import io.github.bumblesoftware.fastload.abstraction.tool.StoreValueFunction;
import io.github.bumblesoftware.fastload.config.DefaultConfig;
import io.github.bumblesoftware.fastload.util.Action;
import io.github.bumblesoftware.fastload.util.Bound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.function.Function;

import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

public class Client119 extends Client1182 {
    @Override
    public String getCompatibleVersions() {
        return "1.19, 1.19.1, 1.19.2";
    }

    @Override
    public int getViewDistance() {
        if (getClientInstance().options == null) {
            return DefaultConfig.LOCAL_CHUNK_RADIUS_BOUND.max();
        } else return getClientInstance().options.getClampedViewDistance();
    }

    @Override
    public <T, X> Screen newConfigScreen(
            final Screen parent,
            X gameOptions,
            final Text title,
            Function<Object[], T[]> options,
            Action config
    ) {
        return new SimpleOptionsScreen(
                parent,
                (GameOptions) gameOptions,
                title,
                (SimpleOption<?>[]) options.apply(new SimpleOption<?>[]{})
        ) {
            @Override
            protected void initFooter() {
                ABSTRACTED_CLIENT.addDrawableChild(this,
                        ABSTRACTED_CLIENT.getNewButton(
                                this.width / 2 - 100,
                                this.height - 27,
                                200, 20,
                                ScreenTexts.DONE,
                                (button) -> {
                                    config.commit();
                                    getClientInstance().setScreen(parent);
                                })
                );
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
