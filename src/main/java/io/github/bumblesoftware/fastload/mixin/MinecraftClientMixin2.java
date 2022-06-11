package io.github.bumblesoftware.fastload.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin2 {

    //Code is from 'kennytv'. All credits are to this person. This is not our code.
    //Permission granted to do so from MIT License of 'forcecloseloadingscreen'.

    @Shadow
    public void setScreen(@Nullable Screen screen) {}

    @Inject(at = @At("HEAD"), method = "setScreen", cancellable = true)
    public void setScreen(final Screen screen, final CallbackInfo ci) {
        if (screen instanceof DownloadingTerrainScreen) {
            ci.cancel();
            setScreen(null);
        }
    }
}
