package io.github.bumblesoftware.fastload.config.modmenu.button;

import com.mojang.serialization.Codec;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.extensions.SimpleVec2i;
import io.github.bumblesoftware.fastload.init.FastLoad;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.propertyKeys.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;

public class FLModMenuButtons {
    private static final String FLB = FastLoad.NAMESPACE.toLowerCase() + ".button.";
    public static SimpleOption<Boolean> getNewBoolButton(String type, boolean getConfig) {
        return SimpleOption.ofBoolean(FLB + type, SimpleOption.constantTooltip(Text.translatable(FLB + type + ".tooltip")), getConfig);
    }
    public static SimpleOption<Integer> getNewSlider(String type, SimpleVec2i vec2i , int defVal) {
        int max = vec2i.max();
        int min = vec2i.min();
        return new SimpleOption<>(FLB + type,
                SimpleOption.constantTooltip(Text.translatable(FLB + type + ".tooltip")),
                (optionText,value) -> {
                    if (value == min) {
                        return GameOptions.getGenericValueText(optionText, Text.translatable(FLB + type + ".min"));
                    } else if (value.equals(max)) {
                        return GameOptions.getGenericValueText(optionText, Text.translatable(FLB + type + ".max"));
                    } else {
                        return GameOptions.getGenericValueText(optionText, value);
                    }
                },
                new SimpleOption.ValidatingIntSliderCallbacks(min,max),
                Codec.DOUBLE.xmap(value -> max, value -> (double)value - max),
                 defVal,value -> {
                    // Set value to other field
                }
            );
    }

    /**
     * Refer to DefaultConfig.propertyKeys.add[] for arrangement of this array!
     */
    public static SimpleOption<?>[] buttons = {
            getNewBoolButton(debug(), getDebug()),
            getNewBoolButton(unsafeClose(), getCloseUnsafe()),
            getNewSlider(render(), getRadiusBound(), getPreRenderRadius()),
            getNewSlider(pregen(),getRadiusBound(), getPregenRadius(true)),
            getNewSlider(tryLimit(), FLMath.getChunkTryLimitBound(), getChunkTryLimit())
    };

    public static SimpleOption<?>[] asOptions() {
        ArrayList<SimpleOption<?>> options = new ArrayList<>(Arrays.asList(buttons));
        return options.toArray(SimpleOption<?>[]::new);
    }
}
