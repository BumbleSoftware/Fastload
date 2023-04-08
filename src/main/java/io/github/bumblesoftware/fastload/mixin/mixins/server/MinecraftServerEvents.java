package io.github.bumblesoftware.fastload.mixin.mixins.server;

import io.github.bumblesoftware.fastload.client.FLClientEvents.RecordTypes.TickEventContext;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.SERVER_TICK_EVENT;


@Mixin(MinecraftServer.class)
public abstract class MinecraftServerEvents {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        SERVER_TICK_EVENT.fireEvent(new TickEventContext(shouldKeepTicking.getAsBoolean()));
    }
}
