package io.github.bumblesoftware.fastload.mixin.mixins.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;


/*
* This code is inspired by: https://github.com/VidTu/Ksyxis of which it's under the MIT License.
* The BumbleSoftware team modified the code.
*/

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow protected abstract void updateMobSpawnOptions();

    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 441))
    private int onPrepareRedirectChunksLoaded(int value) {
        return 0;
    }

    @Redirect(method = "prepareStartRegion", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/WorldGenerationProgressListener;start(Lnet/minecraft/util/math/ChunkPos;)V"))
    private void finishEarly(WorldGenerationProgressListener worldGenerationProgressListener, ChunkPos chunkPos) {
        worldGenerationProgressListener.stop();
        updateMobSpawnOptions();
    }
}
