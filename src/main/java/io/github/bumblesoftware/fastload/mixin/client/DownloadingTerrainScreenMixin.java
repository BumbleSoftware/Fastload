package io.github.bumblesoftware.fastload.mixin.client;

import io.github.bumblesoftware.fastload.util.ObjectHolder;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.DTS_TICK;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.BOOLEAN_EVENT;


@Mixin(DownloadingTerrainScreen.class)
public class DownloadingTerrainScreenMixin {
    @Shadow
    private boolean closeOnNextTick;

    @Inject(at = @At("HEAD"), method = "setReady")
    public void tick(final CallbackInfo ci) {
        final var returnValue = new ObjectHolder<>(closeOnNextTick);
        if (BOOLEAN_EVENT.isNotEmpty(DTS_TICK))
            BOOLEAN_EVENT.fireEvent(List.of(DTS_TICK), returnValue);
        closeOnNextTick = returnValue.heldObj;
    }
}