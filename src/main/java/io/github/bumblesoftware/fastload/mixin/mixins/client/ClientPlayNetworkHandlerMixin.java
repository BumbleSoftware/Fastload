package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.client.BuildingTerrainScreen;
import io.github.bumblesoftware.fastload.client.FLClientEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.PLAYER_JOIN_EVENT;
import static io.github.bumblesoftware.fastload.config.FLMath.*;
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

/**
 * Sets setPlayerJoined to true when the player joins the game
 */
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;addPlayer(ILnet/minecraft/client/network/AbstractClientPlayerEntity;)V"))
    private void onGamedJoinEvent(GameJoinS2CPacket packet, CallbackInfo ci) {
        PLAYER_JOIN_EVENT.fireEvent(new FLClientEvents.RecordTypes.PlayerJoinEventContext(packet));
    }

    @Redirect(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void modifyDownloadingTerrainScreen(MinecraftClient client, Screen screen) {
        if (ABSTRACTED_CLIENT.isSingleplayer()) {
            if (!isLocalRenderEnabled())
                ABSTRACTED_CLIENT.setScreen(screen);
        } else {
            if (isServerRenderEnabled())
                ABSTRACTED_CLIENT.setScreen(ABSTRACTED_CLIENT.newBuildingTerrainScreen(getServerRenderChunkArea()));
            else {
                if (isInstantLoadEnabled())
                    ABSTRACTED_CLIENT.setScreen(null);
                else ABSTRACTED_CLIENT.setScreen(screen);
            }
        }
    }

    @Redirect(method = "onPlayerRespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void instantLoad(MinecraftClient instance, Screen screen) {
        if (!isInstantLoadEnabled())
            ABSTRACTED_CLIENT.setScreen(screen);
    }

    @Redirect(method = "onResourcePackSend", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;execute(Ljava/lang/Runnable;)V"))
    private void redirectResourcePackScreen(MinecraftClient client, Runnable runnable) {
        if (ABSTRACTED_CLIENT.forCurrentScreen(ABSTRACTED_CLIENT::isBuildingTerrainScreen))
            ((BuildingTerrainScreen)ABSTRACTED_CLIENT.getCurrentScreen()).setClose(() ->
                    client.execute(runnable));
        else client.execute(runnable);
    }

}
