package com.abdelaziz.fastload.mixin;

import com.abdelaziz.fastload.FLMath;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import net.minecraft.server.WorldGenerationProgressLogger;

@Mixin(WorldGenerationProgressLogger.class)
public class WorldGenerationProcessLoggerMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static int injected(int radius){
        return FLMath.getSetSpawnChunkRadius();
    }
}
