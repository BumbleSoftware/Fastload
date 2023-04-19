package io.github.bumblesoftware.fastload.client;

import io.github.bumblesoftware.fastload.api.events.AbstractEvent;
import io.github.bumblesoftware.fastload.api.events.CapableEvent;
import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.PlayerJoinEventContext;
import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.SetScreenEventContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stores important events based on {@link AbstractEvent} that fastload uses.
 * Client events.
 */
public interface FLClientEvents {
    static void init() {}

    interface Events {
        AbstractEvent<SetScreenEventContext> SET_SCREEN_EVENT = new CapableEvent<>();
        AbstractEvent<PlayerJoinEventContext> PLAYER_JOIN_EVENT = new CapableEvent<>();
    }

    interface Locations {
        String LLS_441_REDIRECT = "level_loading_screen;441;redirect;";
        String RESPAWN_DTS_REDIRECT = "respawn_dts_redirect;";
        String DTS_GAME_JOIN_REDIRECT = "client_player_network_handler;on_game_join;downloading_terrain_screen;" +
                "redirect";
        String PROGRESS_SCREEN_JOIN_WORLD_REDIRECT = "minecraft_client;join_world;progress_screen;redirect";
        String CLIENT_PLAYER_INIT = "client_player;init;";
        String RENDER_TICK = "minecraft_client;render_tick;";
    }

    interface Contexts {
        record PlayerJoinEventContext(GameJoinS2CPacket packet) {}
        record SetScreenEventContext(Screen screen, CallbackInfo ci) {}
    }
}
