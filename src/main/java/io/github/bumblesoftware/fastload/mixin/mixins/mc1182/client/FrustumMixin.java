package io.github.bumblesoftware.fastload.mixin.mixins.mc1182.client;

import io.github.bumblesoftware.fastload.common.FLCommonEvents.Contexts.BoxBooleanContext;
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
        final var box = new Box(x1, y1, z1, x2, y2, z2);
        final var returnValue = new ObjectHolder<>(cir.getReturnValue());
        if (BOX_BOOLEAN_EVENT.isNotEmpty(FRUSTUM_BOX_BOOL))
            BOX_BOOLEAN_EVENT.fireEvent(List.of(FRUSTUM_BOX_BOOL), new BoxBooleanContext(box, returnValue));
        cir.setReturnValue(returnValue.heldObj);
    }
}
