package com.abdelaziz.fastload.mixin.client;

import com.abdelaziz.fastload.client.FLClientEvents;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Sets setPlayerJoined to true when the player joins the game
 */
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void onGamedJoinEvent(GameJoinS2CPacket packet, CallbackInfo ci) {
        FLClientEvents.PLAYER_JOIN_EVENT.fireEvent(new FLClientEvents.RecordTypes.PlayerJoinEventContext(packet));
    }
}
