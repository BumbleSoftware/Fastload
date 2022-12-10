package io.github.bumblesoftware.fastload.api.events.custom;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

public class FLPauseMenuEvent implements FLAbstractEvent.AbstractEvent<FLPauseMenuEvent.PauseMenuEventArgs> {
    private static final ArrayList<PauseMenuEventArgs> EVENT_HOLDER = new ArrayList<>();

    public void onEvent(boolean pause, CallbackInfo ci) {
        for (PauseMenuEventArgs eventCaller : getEventHolder()) {
            eventCaller.args(pause, ci);
        }
    }

    @Override
    public ArrayList<PauseMenuEventArgs> getEventHolder() {
        return EVENT_HOLDER;
    }


    @FunctionalInterface
    public interface PauseMenuEventArgs extends FLAbstractEvent.EventArgs {
        void args(boolean pause, CallbackInfo ci);
    }
}
