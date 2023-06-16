package io.github.bumblesoftware.fastload.mixin.client;

import io.github.bumblesoftware.fastload.common.FLCommonEvents.Contexts.EmptyContext;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.CLIENT_PLAYER_INIT;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.EMPTY_EVENT;

/**
 * Sets playerLoaded to true when... player loads
 */
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "init", at = @At("HEAD"))
    private void onClientPlayerEntityMixinInitEvent(CallbackInfo ci) {
        if (EMPTY_EVENT.isNotEmpty())
            EMPTY_EVENT.execute(List.of(CLIENT_PLAYER_INIT), new EmptyContext());
    }
}
