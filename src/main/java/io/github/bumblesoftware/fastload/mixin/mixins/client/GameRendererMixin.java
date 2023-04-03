package io.github.bumblesoftware.fastload.mixin.mixins.client;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyConstant(method = "updateWorldIcon(Ljava/nio/file/Path;)V", constant = @Constant(intValue = 10))
    private static int delayWorldIcon(int constant) {
        return 100;
    }
}
