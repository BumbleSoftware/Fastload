package io.github.bumblesoftware.fastload.mixin;

import io.github.bumblesoftware.fastload.FastLoad;
import io.github.bumblesoftware.fastload.config.FLMath;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin {

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawChunkMap(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/gui/WorldGenerationProgressTracker;IIII)V"), index = 3)
    private int fastload_drawCorrectedChunkMap1(int centerY) {
        if (FLMath.getDebug()) FastLoad.LOGGER.info("Corrected LLS ChunkMap");
        return centerY - 10;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawChunkMap(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/gui/WorldGenerationProgressTracker;IIII)V"), index = 5)
    private int fastload_drawCorrectedChunkMap2(int pixelMargin) {
        return 0;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"), index = 4)
    private int fastload_drawCorrectedText(int y) {
        if (FLMath.getDebug()) FastLoad.LOGGER.info("Corrected LLS Text");
        return y - 60;
    }
}