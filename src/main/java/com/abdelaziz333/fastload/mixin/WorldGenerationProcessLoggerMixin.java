package com.abdelaziz333.fastload.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import net.minecraft.server.WorldGenerationProgressLogger;

@Mixin(WorldGenerationProgressLogger.class)
public class WorldGenerationProcessLoggerMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static int injected(int radius){
        return 4;
    }
}
