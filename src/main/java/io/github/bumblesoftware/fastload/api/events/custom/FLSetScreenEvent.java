package io.github.bumblesoftware.fastload.api.events.custom;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

public final class FLSetScreenEvent implements FLAbstractEvent.AbstractEvent<FLSetScreenEvent.SetScreenEventArgs> {
    private static final ArrayList<SetScreenEventArgs> EVENT_HOLDER = new ArrayList<>();

    public void onEvent(final Screen screen, final CallbackInfo ci) {
        for (SetScreenEventArgs eventCaller : getEventHolder()) {
            eventCaller.onSetScreen(screen, ci);
        }
    }

    @Override
    public ArrayList<SetScreenEventArgs> getEventHolder() {
        return EVENT_HOLDER;
    }

    @FunctionalInterface
    public interface SetScreenEventArgs extends FLAbstractEvent.EventArgs {
        void onSetScreen(final Screen screen, final CallbackInfo ci);
    }
}
