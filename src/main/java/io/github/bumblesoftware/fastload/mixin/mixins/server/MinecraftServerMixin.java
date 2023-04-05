package io.github.bumblesoftware.fastload.mixin.mixins.server;

import io.github.bumblesoftware.fastload.client.FLClientEvents.RecordTypes.TickEventContext;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.SERVER_TICK_EVENT;
import static io.github.bumblesoftware.fastload.config.init.FLMath.getPregenArea;
import static io.github.bumblesoftware.fastload.config.init.FLMath.getPregenChunkRadius;


/*
* This code is inspired by: https://github.com/VidTu/Ksyxis of which it's under the MIT License.
* The BumbleSoftware team modified the code to make this possible.
*/

/**
 * Used to change how many chunks should load at 441.
 */
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 441))
    private int onPrepareRedirectChunksLoaded(int value) {
        return getPregenArea();
    }
    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 11))
    private int setRadius(int value) {
        return getPregenChunkRadius(false);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        SERVER_TICK_EVENT.fireEvent(new TickEventContext(shouldKeepTicking.getAsBoolean()));
    }
}
