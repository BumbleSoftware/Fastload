package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.config.init.FLMath;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.render.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

@Restriction(require = @Condition(value = "minecraft", versionPredicates = {
        "1.18.2",
        "1.19",
        "1.19.1",
        "1.19.2"
}))
@Mixin(Frustum.class)
public class FrustumMixin {
    @Inject(method = "isAnyCornerVisible", at = @At("HEAD"), cancellable = true)
    private void setVisible(float x1, float y1, float z1, float x2, float y2, float z2, CallbackInfoReturnable<Boolean> cir) {
        if (ABSTRACTED_CLIENT.forCurrentScreen(ABSTRACTED_CLIENT::isBuildingTerrainScreen) &&
                FLMath.isPreRenderEnabled())
            cir.setReturnValue(true);
    }
}
