package io.github.bumblesoftware.fastload.api.events.custom;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

public class FLRenderTickEvent implements FLAbstractEvent.AbstractEvent<FLRenderTickEvent.RenderTickEventArgs> {
    private static final ArrayList<RenderTickEventArgs> EVENT_HOLDER = new ArrayList<>();

    public void onEvent(boolean tick, CallbackInfo ci) {
        for (RenderTickEventArgs eventCaller : getEventHolder()) {
            eventCaller.args(tick, ci);
        }
    }

    @Override
    public ArrayList<RenderTickEventArgs> getEventHolder() {
        return EVENT_HOLDER;
    }

    @FunctionalInterface
    public interface RenderTickEventArgs extends FLAbstractEvent.EventArgs {
        void args(boolean tick, CallbackInfo ci);
    }
}
