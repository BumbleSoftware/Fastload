package com.abdelaziz333.fastload.mixin;

import com.abdelaziz.fastload.MinecraftClientMixinInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Shadow @Final
    protected MinecraftClient client;
    @Inject(method = "init", at = @At("HEAD"))
    private void setPlayerReady(CallbackInfo ci) {
        ((MinecraftClientMixinInterface)client).canPlayerLoad();
    }
}
