package io.github.bumblesoftware.fastload.client;

import io.github.bumblesoftware.fastload.api.events.AbstractEvent;
import io.github.bumblesoftware.fastload.api.events.CapableEvent;
import io.github.bumblesoftware.fastload.client.FLClientEvents.RecordTypes.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stores important events based on {@link AbstractEvent}
 * that fastload uses. Feel free to make your own events by using this as an example.
 */
public class FLClientEvents {
    public static void init() {}

    public interface EventLocations {
        String LLS441Redirect = "LevelLoadingScreen_replace_via_redirect";
    }

    public interface Events {
        AbstractEvent<Empty> CLIENT_PLAYER_INIT_EVENT =  new CapableEvent<>();
        AbstractEvent<SetScreenEventContext> SET_SCREEN_EVENT = new CapableEvent<>();
        AbstractEvent<TickEventContext> RENDER_TICK_EVENT = new CapableEvent<>();
        AbstractEvent<TickEventContext> SERVER_TICK_EVENT = new CapableEvent<>();
        AbstractEvent<PauseMenuEventContext> PAUSE_MENU_EVENT = new CapableEvent<>();
        AbstractEvent<PlayerJoinEventContext> PLAYER_JOIN_EVENT = new CapableEvent<>();
    }

    public static class RecordTypes {
        public record Empty() {}
        public record PlayerJoinEventContext(GameJoinS2CPacket packet) {}
        public record TickEventContext(boolean tick) {}
        public record SetScreenEventContext(Screen screen, CallbackInfo ci) {}
        public record PauseMenuEventContext(boolean pause, CallbackInfo ci) {}
    }
}
