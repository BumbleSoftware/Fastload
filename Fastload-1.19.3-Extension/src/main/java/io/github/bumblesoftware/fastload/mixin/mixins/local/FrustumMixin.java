package io.github.bumblesoftware.fastload.mixin.mixins.local;

import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.client.sceen.BuildingTerrainScreen;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition(value = "minecraft", versionPredicates = ">=1.19.3"))
@Mixin(Frustum.class)
public class FrustumMixin {
    @Inject(method = "isVisible(DDDDDD)Z", at = @At("HEAD"), cancellable = true)
    private void yes(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, CallbackInfoReturnable<Boolean> cir) {
        if (
                FLMath.isPreRenderEnabled() &&
                MinecraftClient.getInstance().currentScreen instanceof BuildingTerrainScreen
        ) cir.setReturnValue(true);
    }
}
