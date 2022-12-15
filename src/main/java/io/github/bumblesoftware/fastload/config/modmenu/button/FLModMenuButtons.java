package io.github.bumblesoftware.fastload.config.modmenu.button;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.MinMaxHolder;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.propertyKeys.*;
import static io.github.bumblesoftware.fastload.config.init.FLConfig.storeProperty;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;

public class FLModMenuButtons {
    /**
     *  This just stores the .properties address in order by method call. This is done in order
     *  to make it safe to iterate through the values in order to write it to disk.
     */
    private static final Map<String, String> addressStorage = new HashMap<>();
    private static final String FLB = Fastload.NAMESPACE.toLowerCase() + ".button.";

    private static void putStorage(String address, String value) {
        addressStorage.put(address, value);
    }
    private static String getStorage(String address) {
        return addressStorage.get(address);
    }


    /**
     * Next 2 method just create the required buttons, nothing really special aside  from the local variables to ensure
     * naming convention consistency
     */
    public static Option getNewBoolButton(String type, boolean getConfig) {
        System.out.println(type + ":" + getConfig);
        addressStorage.putIfAbsent(type, Boolean.toString(getConfig));
        return CyclingOption.create(
                FLB + type,
                new TranslatableText(FLB + type + ".tooltip"),
                gameOptions -> getConfig,
                (gameOptions, option, value) -> storeProperty(type, value.toString())
        );
    }
    /**
     * SimpleVec2i is just a simple class in package: io.github.bumblesoftware.fastload.extensions that holds two variables
     * as it doesn't make sense for min and maxes to be seperated. Refer to the class for more info!
     */
    public static Option getNewSlider(String type, MinMaxHolder holder , int defVal) {
        int max = holder.max();
        int min = holder.min();
        addressStorage.putIfAbsent(type, Integer.toString(defVal));
        return new DoubleOption(
                FLB + type,
                min,
                max,
                1.0F,
                gameOptions -> Double.parseDouble(getStorage(type)),
                (gameOptions, aDouble) -> {
                    String dbl = Integer.toString(aDouble.intValue());
                    putStorage(type, dbl);
                    FLConfig.storeProperty(type, dbl);
                },
                (gameOptions, option) -> {
                    double d = option.get(gameOptions);
                    if (d == min) {
                        return option.getGenericLabel(new TranslatableText(FLB + type + ".min"));
                    } else {
                        return d == option.getMax() ? option.getGenericLabel(new TranslatableText(FLB + type + ".max")) : option.getGenericLabel((int)d);
                    }
                },
                minecraftClient -> minecraftClient.textRenderer.wrapLines(
                        StringVisitable.plain(new TranslatableText(FLB + type + ".tooltip").getString()),
                        200
                ));
    }

    /**
     * Options getter for screens that use this
     */
    public static Option[] asOptions() {
        Option[] buttons = {
                getNewBoolButton(debug(), getDebug()),
                getNewBoolButton(unsafeClose(), getCloseUnsafe()),
                getNewSlider(render(), getRadiusBound(), getPreRenderRadius()),
                getNewSlider(pregen(),getRadiusBound(), getPregenRadius(true)),
                getNewSlider(tryLimit(), FLMath.getChunkTryLimitBound(), getChunkTryLimit())
        };
        ArrayList<Option> options = new ArrayList<>(Arrays.asList(buttons));
        return options.toArray(Option[]::new);
    }
}
