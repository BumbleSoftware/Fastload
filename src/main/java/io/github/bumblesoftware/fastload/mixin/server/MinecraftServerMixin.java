package io.github.bumblesoftware.fastload.mixin.server;

import io.github.bumblesoftware.fastload.common.FLCommonEvents;
import io.github.bumblesoftware.fastload.util.obj_holders.MutableObjectHolder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.*;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Locations.PREPARE_START_REGION;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Locations.SERVER_TICK;

/*
* This code is inspired by: https://github.com/VidTu/Ksyxis of which it's under the MIT License.
* The BumbleSoftware team modified the code.
*/
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(method = "tickServer", at = @At("HEAD"))
    private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (BOOLEAN_EVENT.isNotEmpty(PREPARE_START_REGION))
            BOOLEAN_EVENT.execute(List.of(SERVER_TICK), new MutableObjectHolder<>(shouldKeepTicking.getAsBoolean()));
    }

    @ModifyConstant(method = "prepareLevels", constant = @Constant(intValue = 441))
    private int modify_prepareStartRegion_chunkCount(int value) {
        final var returnValue = new MutableObjectHolder<>(441);
        if (INTEGER_EVENT.isNotEmpty(PREPARE_START_REGION))
            INTEGER_EVENT.execute(List.of(PREPARE_START_REGION), true, returnValue);
        return returnValue.getHeldObj();
    }

    @Redirect(method = "prepareLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/progress/ChunkProgressListener;updateSpawnPos(Lnet/minecraft/world/level/ChunkPos;)V"))
    private void handleProgressListener(ChunkProgressListener worldGenerationProgressListener, ChunkPos chunkPos) {
        if (PROGRESS_LISTENER_EVENT.isNotEmpty(PREPARE_START_REGION))
            PROGRESS_LISTENER_EVENT.execute(
                    new FLCommonEvents.Contexts.ProgressListenerContext(worldGenerationProgressListener, chunkPos)
            );
    }
}
