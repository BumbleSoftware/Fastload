package io.github.bumblesoftware.fastload.mixin;

import io.github.bumblesoftware.fastload.FLMath;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin {
    @ModifyConstant(method = "render", constant = @Constant(intValue = 30))
    private int fixPercentage(int constant) {
        return 30 + FLMath.getSetSpawnChunkRadius()/2;
    }

}
