package com.abdelaziz.fastload.mixin;

import com.abdelaziz.fastload.config.FLMath;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import net.minecraft.server.WorldGenerationProgressLogger;


@Mixin(value = WorldGenerationProgressLogger.class, priority = 1200)
public class WorldGenerationProcessLoggerMixin {
    @Shadow
    @Final
    @Mutable
    private int totalCount = FLMath.getProgressArea();
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static int setRadius(int radius) {
        return FLMath.getPregenRadius(false);
    }
    @Shadow private int generatedCount;
    /**
     * @author Fluffy Bumblebee
     * @reason Cancel C2ME's interference
     */
    @Overwrite
    public int getProgressPercentage() {
        return MathHelper.floor((float)this.generatedCount * 100.0F / (float)this.totalCount);
    }
}