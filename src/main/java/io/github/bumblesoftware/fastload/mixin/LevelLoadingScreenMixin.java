package io.github.bumblesoftware.fastload.mixin;

import io.github.bumblesoftware.fastload.config.FLMath;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin {
    @Shadow
    public static void drawChunkMap(MatrixStack matrices, WorldGenerationProgressTracker progressProvider, int centerX, int centerY, int pixelSize, int pixelMargin) {
    }
    private final int correctedPixelSize = 1;


    @ModifyConstant(method = "render", constant = @Constant(intValue = 30))
    private int fixPercentage(int constant) {
        return 15 + FLMath.getPregenRadius(true)/3;
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 2))
    private int fixPixelMargin(int constant) {
        return correctedPixelSize;
    }
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawChunkMap(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/gui/WorldGenerationProgressTracker;IIII)V"))
    private void drawCorrectedChunkMap(MatrixStack matrices, WorldGenerationProgressTracker progressProvider, int centerX, int centerY, int pixelSize, int pixelMargin) {
        drawChunkMap(matrices, progressProvider,  centerX/2, centerY/2 + 30, correctedPixelSize, 0);
    }
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCorrectedText(MatrixStack matrixStack, TextRenderer textRenderer, String s, int x, int y, int colour) {
        DrawableHelper.drawCenteredText(matrixStack, textRenderer, s, x/2, y/2 - 30, colour);
    }
}
