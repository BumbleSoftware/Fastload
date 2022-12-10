package io.github.bumblesoftware.fastload.api.events;

import io.github.bumblesoftware.fastload.api.events.FLEvents.RecordTypes.*;
import io.github.bumblesoftware.fastload.api.events.abstract_events.FLGenericEvent;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class FLEvents {
    public static final FLGenericEvent<SetScreenEventContext> SET_SCREEN_EVENT = new FLGenericEvent<>();
    public static final FLGenericEvent<RenderTickEventContext> RENDER_TICK_EVENT = new FLGenericEvent<>();
    public static final FLGenericEvent<PauseMenuEventContext> PAUSE_MENU_EVENT = new FLGenericEvent<>();

    public static class RecordTypes {
        public record SetScreenEventContext(Screen screen, CallbackInfo ci) {}
        public record RenderTickEventContext(boolean tick, CallbackInfo ci) {}
        public record PauseMenuEventContext(boolean tick, CallbackInfo ci) {}
    }
}
