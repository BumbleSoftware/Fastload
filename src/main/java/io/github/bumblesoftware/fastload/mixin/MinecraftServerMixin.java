package io.github.bumblesoftware.fastload.mixin;

import io.github.bumblesoftware.fastload.config.FLMath;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


/*
* This code is inspired by: https://github.com/VidTu/Ksyxis of which it's under the MIT License.
* The BumbleSoftware team modified the code to make this possible.
*/

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 441))
    private int onPrepareRedirectChunksLoaded(int value) {
        return FLMath.getPregenArea();
    }
    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 11))
    private int setRadius(int value) {
        return FLMath.getPregenRadius();
    }
}
