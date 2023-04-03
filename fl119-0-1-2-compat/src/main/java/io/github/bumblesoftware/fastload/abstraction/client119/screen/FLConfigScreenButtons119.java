package io.github.bumblesoftware.fastload.abstraction.client119.screen;

import com.mojang.serialization.Codec;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.MinMaxHolder;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.propertyKeys.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;

public class FLConfigScreenButtons119 {
    /**
     *  This just stores the .properties address in order by method call. This is done in order
     *  to make it safe to iterate through the values in order to write it to disk.
     */
    private static final ArrayList<String> addressStorage = new ArrayList<>();
    private static final String FLB = Fastload.NAMESPACE.toLowerCase() + ".button.";

    /**
     * This method is a thing because it's the only thing other classes would need in order to
     * iterate through returned values from FLConfigScreen
     */
    public static String getButtonAddresses(int i) {
        return addressStorage.get(i);
    }

    /**
     * Next 2 method just create the required buttons, nothing really special aside  from the local variables to ensure
     * naming convention consistency
     */
    public static SimpleOption<Boolean> getNewBoolButton(String type, boolean getConfig) {
        addressStorage.add(type);
        return SimpleOption.ofBoolean(FLB + type, SimpleOption.constantTooltip(Text.translatable(FLB + type + ".tooltip")), getConfig);
    }
    /**
     * SimpleVec2i is just a simple class in package: io.github.bumblesoftware.fastload.extensions that holds two variables
     * as it doesn't make sense for min and maxes to be seperated. Refer to the class for more info!
     */
    public static SimpleOption<Integer> getNewSlider(String type, MinMaxHolder holder , int defVal) {
        addressStorage.add(type);
        int max = holder.max();
        int min = holder.min();
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
     * This array determines the ORDER OF BUTTONS
     * Designed to be iterable
     */
    protected static final SimpleOption<?>[] buttons = {
            getNewBoolButton(debug(), isDebugEnabled()),
            getNewBoolButton(unsafeClose(), isForceCloseEnabled()),
            getNewSlider(render(), getRadiusBound(), getPreRenderRadius()),
            getNewSlider(pregen(),getRadiusBound(), getPregenRadius(true)),
            getNewSlider(tryLimit(), FLMath.getChunkTryLimitBound(), getChunkTryLimit())
    };

    /**
     * Options getter for screens that use this
     */
    public static SimpleOption<?>[] asOptions() {
        ArrayList<SimpleOption<?>> options = new ArrayList<>(Arrays.asList(buttons));
        return options.toArray(SimpleOption<?>[]::new);
    }
}
