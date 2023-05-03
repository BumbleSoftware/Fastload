package io.github.bumblesoftware.fastload.compat.modmenu;

import io.github.bumblesoftware.fastload.abstraction.AbstractClientCalls;
import io.github.bumblesoftware.fastload.config.FLConfig;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.Bound;

import java.util.List;

import static io.github.bumblesoftware.fastload.config.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.init.FastloadClient.MINECRAFT_ABSTRACTION_HANDLER;

public final class FLConfigScreenButtons<Option> {
    public static final AbstractClientCalls ABSTRACTED_CLIENT = MINECRAFT_ABSTRACTION_HANDLER.directory.getAbstractedEntries();

    private final String NAMESPACE_BUTTON = Fastload.NAMESPACE.toLowerCase() + ".button.";

    public Option getNewBoolButton(final String identifier) {
        return ABSTRACTED_CLIENT.newCyclingButton(
                NAMESPACE_BUTTON,
                identifier,
                FLConfig::retrieveProperty,
                FLConfig::storeProperty
        );
    }

    public Option getNewSlider(final String identifier, final Bound minMaxValues) {
        return ABSTRACTED_CLIENT.newSlider(
                NAMESPACE_BUTTON,
                identifier,
                FLConfig::retrieveProperty,
                FLConfig::storeProperty,
                minMaxValues,
                200
        );
    }

    public  Option[] getAllOptions(Option[] array) {
        return List.of(
                getNewBoolButton(DEBUG_KEY),
                getNewBoolButton(INSTANT_LOAD_KEY),
                getNewSlider(LOCAL_RENDER_RADIUS_KEY, LOCAL_CHUNK_RADIUS_BOUND),
                getNewSlider(SERVER_RENDER_RADIUS_KEY, SERVER_CHUNK_RADIUS_BOUND),
                getNewSlider(CHUNK_TRY_LIMIT_KEY, CHUNK_TRY_LIMIT_BOUND)
        ).toArray(array);
    }
}
