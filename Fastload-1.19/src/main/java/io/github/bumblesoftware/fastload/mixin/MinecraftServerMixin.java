package io.github.bumblesoftware.fastload.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.server.MinecraftServer;


/*
* This code is inspired by: https://github.com/VidTu/Ksyxis of which it's under the MIT License.
* The BumbleSoftware team modified the code to make this possible.
*/

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 441))
    public int onPrepareRedirectChunksLoaded(int value) {
        return 25;
    }
}