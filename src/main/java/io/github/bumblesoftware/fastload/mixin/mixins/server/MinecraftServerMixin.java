package io.github.bumblesoftware.fastload.mixin.mixins.server;

import io.github.bumblesoftware.fastload.client.FLClientEvents.RecordTypes.TickEventContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.SERVER_TICK_EVENT;


/*
* This code is inspired by: https://github.com/VidTu/Ksyxis of which it's under the MIT License.
* The BumbleSoftware team modified the code.
*/

/**
 * Used to change how many chunks should load at 441.
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

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        SERVER_TICK_EVENT.fireEvent(new TickEventContext(shouldKeepTicking.getAsBoolean()));
    }
}
