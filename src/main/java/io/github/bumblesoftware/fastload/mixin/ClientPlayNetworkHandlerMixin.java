package io.github.bumblesoftware.fastload.mixin;

import io.github.bumblesoftware.fastload.MinecraftClientMixinInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow @Final
    private MinecraftClient client;
    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void onGamedJoined(GameJoinS2CPacket packet, CallbackInfo ci) {
        ((MinecraftClientMixinInterface)client).gameJoined();
    }
}
