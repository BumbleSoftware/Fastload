package io.github.bumblesoftware.fastload.mixin.mixins.server;

import io.github.bumblesoftware.fastload.config.init.FLMath;
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
        return FLMath.getPregenChunkRadius(false);
    }
    @Shadow private int generatedCount;
    /**
     * @author StockiesLad
     * @reason Fix bug with C2ME
     */
    @Overwrite
    public int getProgressPercentage() {
        return MathHelper.floor((float)this.generatedCount * 100.0F / (float)FLMath.getProgressArea());
    }
}
