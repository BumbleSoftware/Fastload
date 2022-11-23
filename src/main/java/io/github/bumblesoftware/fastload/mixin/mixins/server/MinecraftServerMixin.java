package io.github.bumblesoftware.fastload.mixin.mixins.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static io.github.bumblesoftware.fastload.config.init.FLMath.getPregenArea;
import static io.github.bumblesoftware.fastload.config.init.FLMath.getPregenRadius;


/*
* This code is inspired by: https://github.com/VidTu/Ksyxis of which it's under the MIT License.
* The BumbleSoftware team modified the code to make this possible.
*/

/**
 * Used to change how many chunks should load at 441.
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 441))
    private int onPrepareRedirectChunksLoaded(int value) {
        return getPregenArea();
    }
    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 11))
    private int setRadius(int value) {
        return getPregenRadius(false);
    }
}
