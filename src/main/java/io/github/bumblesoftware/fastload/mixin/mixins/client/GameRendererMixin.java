package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyConstant(method = "updateWorldIcon(Ljava/nio/file/Path;)V", constant = @Constant(intValue = 10))
    private static int delayWorldIcon(int constant) {
        if (FLMath.isDebugEnabled())
            Fastload.LOGGER.info("worldIcon time prolonged");
        return 100;
    }
}
