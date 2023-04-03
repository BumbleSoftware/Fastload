package io.github.bumblesoftware.fastload.abstraction.client1182.screen;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.mixin.mixins.client.OptionAccess;
import io.github.bumblesoftware.fastload.util.MinMaxHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.Arrays;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.config.init.FLConfig.storeProperty;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;

public class FLConfigScreenButtons1182 {
    /**
     *  This just stores the .properties address in order by method call. This is done in order
     *  to make it safe to onElement through the values in order to write it to disk.
     */
    private static final Object2ObjectArrayMap<String, String> ADDRESS_STORAGE = new Object2ObjectArrayMap<>();
    private static final String FLB = Fastload.NAMESPACE.toLowerCase() + ".button.";

    private static void putStorage(String address, String value) {
        ADDRESS_STORAGE.put(address, value);
    }
    private static String getStorage(String address) {
        return ADDRESS_STORAGE.get(address);
    }


    /**
     * Next 2 method just create the required buttons, nothing really special aside  from the local variables to ensure
     * naming convention consistency
     */
    public static Option getNewBoolButton(String type, boolean getConfig) {
        ADDRESS_STORAGE.putIfAbsent(type, Boolean.toString(getConfig));
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
        ADDRESS_STORAGE.putIfAbsent(type, Integer.toString(defVal));
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
                        return ((OptionAccess)option).getGenericLabelProxy(new TranslatableText(FLB + type + ".min"));
                    } else {
                        return d == option.getMax() ?
                                ((OptionAccess)option).getGenericLabelProxy(new TranslatableText(FLB + type + ".max")) :
                                ((OptionAccess)option).getGenericLabelProxy(new LiteralText(Integer.toString((int)d)));
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
                getNewBoolButton(DEBUG_KEY, isDebugEnabled()),
                getNewBoolButton(FORCE_CLOSE_KEY, isForceCloseEnabled()),
                getNewSlider(RENDER_RADIUS_KEY, getRadiusBound(), getPreRenderRadius()),
                getNewSlider(PREGEN_RADIUS_KEY,getRadiusBound(), getPregenRadius(true)),
                getNewSlider(TRY_LIMIT_KEY, FLMath.getChunkTryLimitBound(), getChunkTryLimit())
        };
        ArrayList<Option> options = new ArrayList<>(Arrays.asList(buttons));
        return options.toArray(Option[]::new);
    }
}
