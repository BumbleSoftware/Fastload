package io.github.bumblesoftware.fastload.mixin;

import io.github.bumblesoftware.fastload.config.FLMath;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import net.minecraft.server.WorldGenerationProgressLogger;


@Mixin(value = WorldGenerationProgressLogger.class, priority = 1200)
public class WorldGenerationProcessLoggerMixin {
    int anInt = FLMath.getSquareArea(1);
    @Shadow @Final @Mutable private int totalCount = anInt;
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static int setRadius(int radius) {
        return FLMath.getPregenRadius();
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
