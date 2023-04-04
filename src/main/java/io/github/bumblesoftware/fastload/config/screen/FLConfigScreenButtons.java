package io.github.bumblesoftware.fastload.config.screen;

import io.github.bumblesoftware.fastload.abstraction.client1182.RetrieveValueFunction;
import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.init.FastloadClient;
import io.github.bumblesoftware.fastload.util.MinMaxHolder;

import java.util.List;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;

public class FLConfigScreenButtons<Option> {

    private final String NAMESPACE_BUTTON = Fastload.NAMESPACE.toLowerCase() + ".button.";

    public Option getNewBoolButton(String identifier, RetrieveValueFunction getProperty) {
        return FastloadClient.ABSTRACTED_CLIENT.newCyclingButton(
                NAMESPACE_BUTTON, identifier, getProperty, FLConfig::storeProperty
        );
    }

    public  Option getNewSlider(String identifier, MinMaxHolder holder, RetrieveValueFunction getProperty) {
        return FastloadClient.ABSTRACTED_CLIENT.newSlider(
                NAMESPACE_BUTTON, identifier, holder,  getProperty, FLConfig::storeProperty, 200
        );
    }

    public  Option[] getAllOptions(Option[] array) {
        return List.of(
                getNewBoolButton(DEBUG_KEY, (key) -> isDebugEnabled().toString()),
                getNewBoolButton(FORCE_CLOSE_KEY, (key) -> isForceCloseEnabled().toString()),
                getNewSlider(RENDER_RADIUS_KEY, getRadiusBound(),
                        (key) -> Integer.toString(getRenderChunkRadius(true))),
                getNewSlider(PREGEN_RADIUS_KEY, getRadiusBound(), (key) -> Integer.toString(getPregenChunkRadius(true))),
                getNewSlider(TRY_LIMIT_KEY, getChunkTryLimitBound(),
                        (key) -> Integer.toString(getChunkTryLimit()))
        ).toArray(array);
    }
}
