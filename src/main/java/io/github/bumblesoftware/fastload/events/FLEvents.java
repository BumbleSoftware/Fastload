package io.github.bumblesoftware.fastload.events;

import io.github.bumblesoftware.fastload.api.events.AbstractEvent.DefaultEvent;
import io.github.bumblesoftware.fastload.events.FLEvents.RecordTypes.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stores important events that fastload uses. Feel free to make your own by using this as an example.
 */
public class FLEvents {
    public static final DefaultEvent<SetScreenEventContext> SET_SCREEN_EVENT = new DefaultEvent<>();
    public static final DefaultEvent<RenderTickEventContext> RENDER_TICK_EVENT = new DefaultEvent<>();
    public static final DefaultEvent<PauseMenuEventContext> PAUSE_MENU_EVENT = new DefaultEvent<>();
    public static final DefaultEvent<Empty> CLIENT_PLAYER_INIT_EVENT = new DefaultEvent<>();
    public static final DefaultEvent<PlayerJoinEventContext> PLAYER_JOIN_EVENT = new DefaultEvent<>();

    /**w
     * Record that holds important params and packages it in one object so that params are consistent among
     * multiple events
     */
    public static class RecordTypes {
        public record Empty() {}
        public record RenderTickEventContext(boolean tick) {}
        public record PlayerJoinEventContext(GameJoinS2CPacket packet) {}
        public record SetScreenEventContext(Screen screen, CallbackInfo ci) {}
        public record PauseMenuEventContext(boolean pause, CallbackInfo ci) {}
    }

}
