package io.github.bumblesoftware.fastload.config.modmenu.button;

import com.mojang.serialization.Codec;
import io.github.bumblesoftware.fastload.config.FLMath;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class MFModMenuButtons {
    public static SimpleOption<Boolean> getNewBoolButton(String type, boolean getConfig) {
        return SimpleOption.ofBoolean("fastload.button." + type, SimpleOption.constantTooltip(Text.translatable("fastload.button." + type + ".tooltip")), getConfig);
    }
    public static SimpleOption<Integer> getNewSlider(String type, int max, int defVal) {
        return new SimpleOption<>("fastload.button." + type,
                SimpleOption.constantTooltip(Text.translatable("fastload.button." + type + ".tooltip")),
                (optionText,value) -> {
                    if (value == 0) {
                        return GameOptions.getGenericValueText(optionText, Text.translatable("fastload.button." + type + ".min"));
                    } else if (value.equals(max)) {
                        return GameOptions.getGenericValueText(optionText, Text.translatable("fastload.button." + type + ".max"));
                    } else {
                        return GameOptions.getGenericValueText(optionText, value);
                    }
                },
                new SimpleOption.ValidatingIntSliderCallbacks(0,max),
                Codec.DOUBLE.xmap(value -> max, value -> (double)value - max),
                 defVal,value -> {
                    // Set value t other field
                }
            );
    }

    public static SimpleOption<?>[] asOptions() {
        ArrayList<SimpleOption<?>> options = new ArrayList<>();

        options.add(getNewBoolButton("debug", FLMath.getDebug()));
        options.add(getNewBoolButton("force_close", FLMath.getCloseUnsafe()));
        options.add(getNewSlider("pre_render", 32, 0));
        options.add(getNewSlider("gen_radius", 32, 10));
        options.add(getNewSlider("try_limit", 1000, 100));

        return options.toArray(SimpleOption<?>[]::new);
    }
}
