// Code burrowed from PixelAgent007, licensed under MIT.

package com.abdelaziz333.fastload.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Mixin(ServerPlayNetworkHandler.class)
public final class ServerPlayNetworkHandlerPlayerSpeedLimitsMixin {

    @ModifyConstant(method = "onPlayerMove", constant = @Constant(floatValue = 100.0F))
    private float getDefaultMaxPlayerSpeed(float speed) {
        return 1000000.0F;
    }

    @ModifyConstant(method = "onPlayerMove", constant = @Constant(floatValue = 300.0F))
    private float getMaxPlayerElytraSpeed(float speed) {
        return 1000000.0F;
    }

    @ModifyConstant(method = "onVehicleMove", constant = @Constant(doubleValue = 100.0))
    private double getMaxPlayerVehicleSpeed(double speed) {
        return 1000000.0F;
    }
}
