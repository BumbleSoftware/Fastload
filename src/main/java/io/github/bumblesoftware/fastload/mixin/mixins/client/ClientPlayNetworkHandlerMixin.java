package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.client.FLClientEvents;
import io.github.bumblesoftware.fastload.config.init.FLMath;
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
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

/**
 * Sets setPlayerJoined to true when the player joins the game
 */
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void onGamedJoinEvent(GameJoinS2CPacket packet, CallbackInfo ci) {
        PLAYER_JOIN_EVENT.fireEvent(new FLClientEvents.RecordTypes.PlayerJoinEventContext(packet));
    }

    @Redirect(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void modifyDownloadingTerrainScreen(MinecraftClient client, Screen screen) {
        if (!ABSTRACTED_CLIENT.forCurrentScreen(ABSTRACTED_CLIENT::isBuildingTerrainScreen))
            if (FLMath.isServerRenderEnabled())
                client.setScreen(ABSTRACTED_CLIENT.newBuildingTerrainScreen());
            else client.setScreen(screen);
    }
}
