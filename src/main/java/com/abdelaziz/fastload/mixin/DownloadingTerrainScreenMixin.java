package com.abdelaziz.fastload.mixin;

import com.abdelaziz.fastload.FastLoad;
import com.abdelaziz.fastload.config.FLMath;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DownloadingTerrainScreen.class)
public class DownloadingTerrainScreenMixin {

    //Code is from 'kennytv'. All credits are to this person. This is not our code.
    //Permission granted to do so from MIT License of 'forcecloseloadingscreen'.
    @Shadow private boolean closeOnNextTick;

    @Inject(at = @At("HEAD"), method = "setReady")
    public void tick(final CallbackInfo ci) {
        if (FLMath.getDebug()) FastLoad.LOGGER.info("DTS will now close on next tick");
        if (FLMath.getCloseUnsafe() || FLMath.getCloseSafe()) closeOnNextTick = true;
    }
}