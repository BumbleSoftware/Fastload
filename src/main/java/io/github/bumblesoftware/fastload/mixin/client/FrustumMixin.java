package io.github.bumblesoftware.fastload.mixin.client;

import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.BoxBooleanContext;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.BOX_BOOLEAN_EVENT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.FRUSTUM_BOX_BOOL;

@Mixin(Frustum.class)
public class FrustumMixin {
    @Inject(method = "isAnyCornerVisible", at = @At("HEAD"), cancellable = true)
    private void setVisible(float x1, float y1, float z1, float x2, float y2, float z2, CallbackInfoReturnable<Boolean> cir) {
        if (BOX_BOOLEAN_EVENT.isNotEmpty(FRUSTUM_BOX_BOOL))
            BOX_BOOLEAN_EVENT.execute(
                    List.of(FRUSTUM_BOX_BOOL),
                    new BoxBooleanContext(
                        new Box(x1, y1, z1, x2, y2, z2),
                        cir
                    )
            );
    }
}
