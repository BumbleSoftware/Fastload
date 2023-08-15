package io.github.bumblesoftware.fastload.mixin.client;

import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.PlayerJoinEventContext;
import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.SetScreenEventContext;
import io.github.bumblesoftware.fastload.util.obj_holders.MutableObjectHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.PLAYER_JOIN_EVENT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.SET_SCREEN_EVENT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.*;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.RUNNABLE_EVENT;

/**
 * Sets setPlayerJoined to true when the player joins the game
 */
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addPlayer(ILnet/minecraft/client/player/AbstractClientPlayer;)V"))
    private void onGamedJoinEvent(ClientboundLoginPacket packet, CallbackInfo ci) {
        if (PLAYER_JOIN_EVENT.isNotEmpty())
            PLAYER_JOIN_EVENT.execute(new PlayerJoinEventContext(packet));
    }

    @Redirect(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    private void modifyDownloadingTerrainScreen(Minecraft client, Screen screen) {
        if (SET_SCREEN_EVENT.isNotEmpty(DTS_GAME_JOIN_REDIRECT))
            SET_SCREEN_EVENT.execute(List.of(DTS_GAME_JOIN_REDIRECT), new SetScreenEventContext(screen, null));
        else client.setScreen(screen);
    }

    @Redirect(method = "handleRespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    private void instantLoad(Minecraft client, Screen screen) {
        if (SET_SCREEN_EVENT.isNotEmpty(RESPAWN_DTS_REDIRECT))
            SET_SCREEN_EVENT.execute(List.of(RESPAWN_DTS_REDIRECT), new SetScreenEventContext(screen, null));
        else client.setScreen(screen);

    }

    @Redirect(method = "handleResourcePack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;execute(Ljava/lang/Runnable;)V"))
    private void redirectResourcePackScreen(Minecraft client, Runnable runnable) {
        if (RUNNABLE_EVENT.isNotEmpty(RP_SEND_RUNNABLE))
            RUNNABLE_EVENT.execute(List.of(RP_SEND_RUNNABLE), new MutableObjectHolder<>(runnable));
        else client.execute(runnable);
    }
}
