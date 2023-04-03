package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.config.screen.BuildingTerrainScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Frustum.class)
public class FrustumMixin {
    @Inject(method = "isAnyCornerVisible", at = @At("HEAD"), cancellable = true)
    private void yes(float x1, float y1, float z1, float x2, float y2, float z2, CallbackInfoReturnable<Boolean> cir) {
        if (
                FLMath.isPreRenderEnabled() &&
                MinecraftClient.getInstance().currentScreen instanceof BuildingTerrainScreen
        ) cir.setReturnValue(true);
    }
}