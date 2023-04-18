package io.github.bumblesoftware.fastload.mixin.mixins.mc1182.client;

import io.github.bumblesoftware.fastload.client.FLClientEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.CLIENT_PLAYER_INIT_EVENT;

/**
 * Sets playerLoaded to true when... player loads
 */
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "init", at = @At("HEAD"))
    private void onClientPlayerEntityMixinInitEvent(CallbackInfo ci) {
        if (CLIENT_PLAYER_INIT_EVENT.isNotEmpty())
            CLIENT_PLAYER_INIT_EVENT.fireEvent(new FLClientEvents.RecordTypes.Empty());
    }
}
