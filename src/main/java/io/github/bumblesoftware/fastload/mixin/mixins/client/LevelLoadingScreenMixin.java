package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
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
        if (FLMath.isDebugEnabled())
            Fastload.LOGGER.info("Corrected LLS ChunkMap");
        return centerY - 10;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawChunkMap(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/gui/WorldGenerationProgressTracker;IIII)V"), index = 5)
    private int drawCorrectedChunkMap2(int pixelMargin) {
        return 0;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"), index = 4)
    private int drawCorrectedText(int y) {
        if (FLMath.isDebugEnabled()) Fastload.LOGGER.info("Corrected LLS Text");
        return y - 60;
    }
}