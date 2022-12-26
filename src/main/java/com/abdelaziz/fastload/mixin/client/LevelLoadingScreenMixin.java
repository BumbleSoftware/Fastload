package com.abdelaziz.fastload.mixin.client;

import com.abdelaziz.fastload.config.init.FLMath;
import com.abdelaziz.fastload.init.Fastload;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Fixes the chunkMap so that 32 pregen render distance can work
 */
@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin {

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawChunkMap(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/gui/WorldGenerationProgressTracker;IIII)V"), index = 3)
    private int drawCorrectedChunkMap1(int centerY) {
        if (FLMath.getDebug()) Fastload.LOGGER.info("Corrected LLS ChunkMap");
        return centerY - 10;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawChunkMap(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/gui/WorldGenerationProgressTracker;IIII)V"), index = 5)
    private int drawCorrectedChunkMap2(int pixelMargin) {
        return 0;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"), index = 4)
    private int drawCorrectedText(int y) {
        if (FLMath.getDebug()) Fastload.LOGGER.info("Corrected LLS Text");
        return y - 60;
    }
}