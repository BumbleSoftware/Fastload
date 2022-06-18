package io.github.bumblesoftware.fastload.mixin;

import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import net.minecraft.server.WorldGenerationProgressLogger;


@Mixin(value = WorldGenerationProgressLogger.class, priority = 1200)
public class WorldGenerationProcessLoggerMixin {
    @Shadow @Final @Mutable private int totalCount = 81;
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static int setRadius(int radius) {
        return 4;
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
