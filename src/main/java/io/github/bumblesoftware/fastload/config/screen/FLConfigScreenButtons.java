package io.github.bumblesoftware.fastload.config.screen;

import io.github.bumblesoftware.fastload.abstraction.tool.RetrieveValueFunction;
import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.MinMaxHolder;

import java.util.List;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

public class FLConfigScreenButtons<Option> {

    private final String NAMESPACE_BUTTON = Fastload.NAMESPACE.toLowerCase() + ".button.";

    public Option getNewBoolButton(
            final String identifier,
            final RetrieveValueFunction getProperty
    ) {
        return ABSTRACTED_CLIENT.newCyclingButton(
                NAMESPACE_BUTTON,
                identifier,
                getProperty,
                FLConfig::storeProperty
        );
    }

    public Option getNewSlider(
            final String identifier,
            final RetrieveValueFunction getProperty,
            final MinMaxHolder minMaxValues
    ) {
        return ABSTRACTED_CLIENT.newSlider(
                NAMESPACE_BUTTON,
                identifier,
                getProperty,
                FLConfig::storeProperty,
                minMaxValues,
                200
        );
    }

    public  Option[] getAllOptions(Option[] array) {
        return List.of(
                getNewBoolButton(DEBUG_KEY, key -> isDebugEnabled().toString()),
                getNewSlider(
                        RENDER_RADIUS_KEY,
                        key -> Integer.toString(getRenderChunkRadius(true)),
                        CHUNK_RADIUS_BOUND
                ),
                getNewSlider(
                        CHUNK_TRY_LIMIT_KEY,
                        key -> Integer.toString(getChunkTryLimit()),
                        CHUNK_TRY_LIMIT_BOUND
                ),
                getNewSlider(
                        SERVER_RENDER_DIVISOR_KEY,
                        key -> Integer.toString(getServerRenderDivisor()),
                        SERVER_RENDER_DIVISOR_BOUND
                )
        ).toArray(array);
    }
}
