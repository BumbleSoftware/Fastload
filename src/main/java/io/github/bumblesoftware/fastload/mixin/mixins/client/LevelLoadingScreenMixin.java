package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.init.FastLoad;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin {
    @Shadow
    public static void drawChunkMap(MatrixStack matrices, WorldGenerationProgressTracker progressProvider, int centerX, int centerY, int pixelSize, int pixelMargin) {
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawChunkMap(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/gui/WorldGenerationProgressTracker;IIII)V"))
    private void drawCorrectedChunkMap(MatrixStack matrices, WorldGenerationProgressTracker progressProvider, int centerX, int centerY, int pixelSize, int pixelMargin) {
        if (FLMath.getDebug()) FastLoad.LOGGER.info("Corrected LLS ChunkMap");
        drawChunkMap(matrices, progressProvider,  centerX, centerY - 10, pixelSize, 0);
    }
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCorrectedText(MatrixStack matrixStack, TextRenderer textRenderer, String s, int x, int y, int colour) {
        if (FLMath.getDebug()) FastLoad.LOGGER.info("Corrected LLS Text");
        DrawableHelper.drawCenteredText(matrixStack, textRenderer, s, x, y - 60, colour);
    }
}
