package com.abdelaziz.fastload.mixin;

import com.abdelaziz.fastload.config.FLMath;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
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
    private int onPrepareRedirectChunksLoaded(int value) {
        setChunkRadius();
        return FLMath.getSpawnChunkArea(0);
    }
    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 11))
    private int setRadius(int value) {
        setChunkRadius();
        return FLMath.getSetSpawnChunkRadius();
    }
    @Shadow @Mutable @Final public static int START_TICKET_CHUNK_RADIUS = 11;
    @Shadow @Mutable @Final private static int START_TICKET_CHUNKS = 441;
    private void setChunkRadius() {
        START_TICKET_CHUNK_RADIUS = 4;
        START_TICKET_CHUNKS = 49;
    }
}
