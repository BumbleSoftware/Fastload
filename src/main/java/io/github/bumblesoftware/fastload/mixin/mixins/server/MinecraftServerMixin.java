package io.github.bumblesoftware.fastload.mixin.mixins.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static io.github.bumblesoftware.fastload.config.FLMath.getPregenArea;
import static io.github.bumblesoftware.fastload.config.FLMath.getPregenRadius;


/*
* This code is inspired by: https://github.com/VidTu/Ksyxis of which it's under the MIT License.
* The BumbleSoftware team modified the code to make this possible.
*/

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow @Final public final static int START_TICKET_CHUNK_RADIUS = getPregenRadius(false);
    @Shadow @Final private final static int START_TICKET_CHUNKS = getPregenArea();

    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 441))
    private int onPrepareRedirectChunksLoaded(int value) {
        return START_TICKET_CHUNKS;
    }
    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 11))
    private int setRadius(int value) {
        return START_TICKET_CHUNK_RADIUS;
    }
}
