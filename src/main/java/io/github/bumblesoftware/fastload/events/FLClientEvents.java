package io.github.bumblesoftware.fastload.events;

import io.github.bumblesoftware.fastload.api.events.EventFactory;
import io.github.bumblesoftware.fastload.events.FLClientEvents.RecordTypes.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stores important events based on {@link EventFactory AbstractEventFactory}
 * that fastload uses. Feel free to make your own events by using this as an example.
 */
public class FLClientEvents {
    public static void init() {}

    public static final EventFactory<Empty> CLIENT_PLAYER_INIT_EVENT = new EventFactory<>();
    public static final EventFactory<SetScreenEventContext> SET_SCREEN_EVENT = new EventFactory<>();
    public static final EventFactory<TickEventContext> RENDER_TICK_EVENT = new EventFactory<>();
    public static final EventFactory<TickEventContext> SERVER_TICK_EVENT = new EventFactory<>();
    public static final EventFactory<PauseMenuEventContext> PAUSE_MENU_EVENT = new EventFactory<>();
    public static final EventFactory<PlayerJoinEventContext> PLAYER_JOIN_EVENT = new EventFactory<>();
    /**
     * Stores the default record types for {@link FLClientEvents}
     */
    public static class RecordTypes {
        public record Empty() {}
        public record PlayerJoinEventContext(GameJoinS2CPacket packet) {}
        public record TickEventContext(boolean tick) {}
        public record SetScreenEventContext(Screen screen, CallbackInfo ci) {}
        public record PauseMenuEventContext(boolean pause, CallbackInfo ci) {}
    }

}
