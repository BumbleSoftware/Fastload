package io.github.bumblesoftware.fastload.config.modmenu.button;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.extensions.SimpleVec2i;
import io.github.bumblesoftware.fastload.init.FastLoad;
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
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;

public class FLModMenuButtons {
    private static final Map<String, String> storage = new HashMap<>();
    private static void putStorage(String address, String value) {
        storage.put(address, value);
    }
    private static String getStorage(String address) {
        return storage.get(address);
    }
    private static final String FLB = FastLoad.NAMESPACE.toLowerCase() + ".button.";
    public static Option getNewBoolButton(String type, boolean getConfig) {
            storage.putIfAbsent(type, Boolean.toString(getConfig));
        return CyclingOption.create(
                FLB + type,
                new TranslatableText(FLB + type + ".tooltip"),
                gameOptions -> Boolean.getBoolean(getStorage(type)),
                (gameOptions, option, value) -> FLConfig.writeToDisk(type, value.toString(), true)
        );
    }
    public static Option getNewSlider(String type, SimpleVec2i vec2i , int defVal) {
        int max = vec2i.max();
        int min = vec2i.min();
        if (getStorage(type) == null)
            putStorage(type, Integer.toString(defVal));
        return new DoubleOption(
                FLB + type,
                min,
                max,
                1.0F,
                gameOptions -> (double) defVal,
                (gameOptions, aDouble) -> FLConfig.writeToDisk(type, Integer.toString(aDouble.intValue()), true),
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
     * Refer to DefaultConfig.propertyKeys.add[] for arrangement of this array!
     */
    public static Option[] buttons = {
            getNewBoolButton(debug(), getDebug()),
            getNewBoolButton(unsafeClose(), getCloseUnsafe()),
            getNewSlider(render(), getRadiusBound(), getPreRenderRadius()),
            getNewSlider(pregen(),getRadiusBound(), getPregenRadius(true)),
            getNewSlider(tryLimit(), FLMath.getChunkTryLimitBound(), getChunkTryLimit())
    };

    public static Option[] asOptions() {
        ArrayList<Option> options = new ArrayList<>(Arrays.asList(buttons));
        return options.toArray(Option[]::new);
    }
}
