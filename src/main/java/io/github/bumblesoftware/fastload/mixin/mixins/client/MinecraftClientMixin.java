package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.init.Fastload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.integrated.IntegratedServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static io.github.bumblesoftware.fastload.config.init.FLMath.*;
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Redirect(method = "startIntegratedServer(Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Function;" +
            "ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V", at = @At(value = "INVOKE", target = "Lnet" +
            "/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 2))
    private void remove441(MinecraftClient client, @Nullable Screen screen) {
        var isPreRenderEnabled = isPreRenderEnabled();
        if (isDebugEnabled())
            Fastload.LOGGER.info("isPreRenderEnabled: " + isPreRenderEnabled);
        if (isPreRenderEnabled) {
            client.setScreen(ABSTRACTED_CLIENT.newBuildingTerrainScreen());
            if (isDebugEnabled()) {
                Fastload.LOGGER.info("DownloadingTerrainScreen -> BuildingTerrainScreen");
                Fastload.LOGGER.info("Goal (Loaded Chunks): " + getPreRenderArea());
            }
        }
        else client.setScreenAndRender(new DownloadingTerrainScreen());
    }

    @Redirect(method = "startIntegratedServer(Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Function;" +
            "ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/integrated/IntegratedServer;isLoading()Z"))
    private boolean removeWait(IntegratedServer integratedServer) {
        return true;
    }

    @Redirect(method = "joinWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void removeProgressScreen(MinecraftClient client, Screen screen) {
    }
}
