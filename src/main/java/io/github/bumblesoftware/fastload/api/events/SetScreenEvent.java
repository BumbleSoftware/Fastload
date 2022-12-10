package io.github.bumblesoftware.fastload.api.events;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

public final class SetScreenEvent {
    private static final ArrayList<ScreenEventCaller> screenEventCallers = new ArrayList<>();
    public static void register(ScreenEventCaller screenEventCaller) {
        screenEventCallers.add(screenEventCaller);
    }
    public static void onSetScreen(final Screen screen, final CallbackInfo ci) {
        for (ScreenEventCaller eventCaller : screenEventCallers) {
            eventCaller.onSetScreen(screen, ci);
        }
    }
    public interface ScreenEventCaller {
        void onSetScreen(final Screen screen, final CallbackInfo ci);
    }
}
