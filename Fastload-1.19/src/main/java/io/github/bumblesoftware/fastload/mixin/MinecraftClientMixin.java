package io.github.bumblesoftware.fastload.mixin;

import net.minecraft.client.MinecraftClient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;tick()V"), argsOnly = true)
    private boolean render(boolean tick) {
        return true;
    }
}