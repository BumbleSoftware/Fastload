package io.github.bumblesoftware.fastload.events;

import io.github.bumblesoftware.fastload.events.FLEvents.RecordTypes.*;
import io.github.bumblesoftware.fastload.api.events.GenericEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stores important events that fastload uses. Feel free to make your own by using this as an example.
 */
public class FLEvents {
    public static final GenericEvent<SetScreenEventContext> SET_SCREEN_EVENT = new GenericEvent<>();
    public static final GenericEvent<RenderTickEventContext> RENDER_TICK_EVENT = new GenericEvent<>();
    public static final GenericEvent<PauseMenuEventContext> PAUSE_MENU_EVENT = new GenericEvent<>();
    public static final GenericEvent<ClientPlayerInitEventContext> CLIENT_PLAYER_INIT_EVENT = new GenericEvent<>();
    public static final GenericEvent<PlayerJoinEventContext> PLAYER_JOIN_EVENT = new GenericEvent<>();

    public static class RecordTypes {
        public record SetScreenEventContext(Screen screen, CallbackInfo ci) {}
        public record RenderTickEventContext(boolean tick, CallbackInfo ci) {}
        public record PauseMenuEventContext(boolean pause, CallbackInfo ci) {}
        public record ClientPlayerInitEventContext(CallbackInfo ci) {}
        public record PlayerJoinEventContext(GameJoinS2CPacket packet, CallbackInfo ci) {}
    }
}
