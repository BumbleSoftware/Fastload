package io.github.bumblesoftware.fastload.mixin.mixins.mc1193.client;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.integrated.IntegratedServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static io.github.bumblesoftware.fastload.config.FLMath.*;
import static io.github.bumblesoftware.fastload.init.Fastload.LOGGER;
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

@Restriction(require = @Condition(value = "minecraft", versionPredicates = ">=1.19.3"))
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Redirect(method = "startIntegratedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void remove441(MinecraftClient client, @Nullable Screen screen) {
        var isPreRenderEnabled = isLocalRenderEnabled();
        if (isDebugEnabled()) {
            LOGGER.info("isLocalRenderEnabled: " + isPreRenderEnabled);
            LOGGER.info("localRenderChunkRadius: " + getLocalRenderChunkRadius());
            LOGGER.info("Fastload Perceived Render Distance: " + ABSTRACTED_CLIENT.getViewDistance());
        }
        if (isPreRenderEnabled) {
            client.setScreen(ABSTRACTED_CLIENT.newBuildingTerrainScreen(getLocalRenderChunkArea()));
            if (isDebugEnabled()) {
                LOGGER.info("DownloadingTerrainScreen -> BuildingTerrainScreen");
                LOGGER.info("Goal (Loaded Chunks): " + getLocalRenderChunkArea());
            }
        }
        else client.setScreen(new DownloadingTerrainScreen());
    }

    @Redirect(method = "startIntegratedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/integrated/IntegratedServer;isLoading()Z"))
    private boolean removeWait(IntegratedServer integratedServer) {
        return true;
    }

    @SuppressWarnings("EmptyMethod")
    @Redirect(method = "joinWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void removeProgressScreen(MinecraftClient client, Screen screen) {}
}
