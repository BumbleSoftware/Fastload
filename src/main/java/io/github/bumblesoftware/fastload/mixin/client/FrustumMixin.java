package io.github.bumblesoftware.fastload.mixin.client;

import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.BoxBooleanContext;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.BOX_BOOLEAN_EVENT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.FRUSTUM_BOX_BOOL;

@Mixin(Frustum.class)
public class FrustumMixin {
    @Inject(method = "cubeInFrustum(DDDDDD)Z", at = @At("HEAD"), cancellable = true)
    private void setVisible(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, CallbackInfoReturnable<Boolean> cir) {
        if (BOX_BOOLEAN_EVENT.isNotEmpty(FRUSTUM_BOX_BOOL))
            BOX_BOOLEAN_EVENT.execute(
                    List.of(FRUSTUM_BOX_BOOL),
                    new BoxBooleanContext(
                            new AABB(minX, minY, minZ, maxX, maxY, maxZ),
                            cir
                    )
            );
    }
}
