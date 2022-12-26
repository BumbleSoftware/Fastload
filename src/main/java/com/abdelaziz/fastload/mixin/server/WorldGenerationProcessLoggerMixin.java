package com.abdelaziz.fastload.mixin.server;

import com.abdelaziz.fastload.config.init.FLMath;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import net.minecraft.server.WorldGenerationProgressLogger;


/**
 * Fixes the progress logger to watch the new amount of chunks to be pregenerated
 */
@Mixin(value = WorldGenerationProgressLogger.class, priority = 1200)
public class WorldGenerationProcessLoggerMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static int setRadius(int radius) {
        return FLMath.getPregenRadius(false);
    }

    @Shadow
    private int generatedCount;

    /**
     * @author Fluffy Bumblebee
     * @reason Cancel C2ME's interference
     */
    @Overwrite
    public int getProgressPercentage() {
        return MathHelper.floor((float) this.generatedCount * 100.0F / (float) FLMath.getProgressArea());
    }
}
