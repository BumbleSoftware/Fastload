package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
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

    /**
     *  Closes Downloading Terrain Screen ASAP, whilst being safe
     */
    @Inject(at = @At("HEAD"), method = "setReady")
    public void tick(final CallbackInfo ci) {
        if (FLMath.isDebugEnabled()) Fastload.LOGGER.info(
                "DownloadingTerrainScreen set to close on next render tick."
        );
        closeOnNextTick = true;
    }
}
