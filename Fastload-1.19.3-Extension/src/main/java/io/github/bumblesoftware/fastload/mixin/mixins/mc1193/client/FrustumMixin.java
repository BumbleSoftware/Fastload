package io.github.bumblesoftware.fastload.mixin.mixins.mc1193.client;

import io.github.bumblesoftware.fastload.common.FLCommonEvents;
import io.github.bumblesoftware.fastload.util.ObjectHolder;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.FRUSTUM_BOX_BOOL;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.BOX_BOOLEAN_EVENT;

@Restriction(require = @Condition(value = "minecraft", versionPredicates = ">=1.19.3"))
@Mixin(Frustum.class)
public class FrustumMixin {
    @Inject(method = "isVisible(DDDDDD)Z", at = @At("HEAD"), cancellable = true)
    private void setVisible(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, CallbackInfoReturnable<Boolean> cir) {
        final var box = new Box(minX, minY, minZ, maxX, maxY, maxZ);
        final var returnValue = new ObjectHolder<>(cir.getReturnValue());
        if (BOX_BOOLEAN_EVENT.isNotEmpty(FRUSTUM_BOX_BOOL))
            BOX_BOOLEAN_EVENT.fireEvent(List.of(FRUSTUM_BOX_BOOL), new FLCommonEvents.Contexts.BoxBooleanContext(box, returnValue));
        cir.setReturnValue(returnValue.heldObj);
    }
}
