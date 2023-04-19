package io.github.bumblesoftware.fastload.mixin.mixins.mc1182.client;

import io.github.bumblesoftware.fastload.client.BuildingTerrainScreen;
import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.PlayerJoinEventContext;
import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.SetScreenEventContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.PLAYER_JOIN_EVENT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.SET_SCREEN_EVENT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.DTS_GAME_JOIN_REDIRECT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.RESPAWN_DTS_REDIRECT;
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

/**
 * Sets setPlayerJoined to true when the player joins the game
 */
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;addPlayer(ILnet/minecraft/client/network/AbstractClientPlayerEntity;)V"))
    private void onGamedJoinEvent(GameJoinS2CPacket packet, CallbackInfo ci) {
        if (PLAYER_JOIN_EVENT.isNotEmpty())
            PLAYER_JOIN_EVENT.fireEvent(new PlayerJoinEventContext(packet));
    }

    @Redirect(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void modifyDownloadingTerrainScreen(MinecraftClient client, Screen screen) {
        if (SET_SCREEN_EVENT.isNotEmpty(DTS_GAME_JOIN_REDIRECT))
            SET_SCREEN_EVENT.fireEvent(List.of(DTS_GAME_JOIN_REDIRECT), new SetScreenEventContext(screen, null));
    }

    @Redirect(method = "onPlayerRespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void instantLoad(MinecraftClient instance, Screen screen) {
        if (SET_SCREEN_EVENT.isNotEmpty(RESPAWN_DTS_REDIRECT))
            SET_SCREEN_EVENT.fireEvent(
                    List.of(RESPAWN_DTS_REDIRECT),
                    new SetScreenEventContext(screen, null)
            );
    }

    @Redirect(method = "onResourcePackSend", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;execute(Ljava/lang/Runnable;)V"))
    private void redirectResourcePackScreen(MinecraftClient client, Runnable runnable) {
        if (ABSTRACTED_CLIENT.forCurrentScreen(ABSTRACTED_CLIENT::isBuildingTerrainScreen))
            ((BuildingTerrainScreen)ABSTRACTED_CLIENT.getCurrentScreen()).setClose(() ->
                    client.execute(runnable));
        else client.execute(runnable);
    }

}
