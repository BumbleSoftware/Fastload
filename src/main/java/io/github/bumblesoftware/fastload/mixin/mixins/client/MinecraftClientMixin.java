package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.config.init.FLMath;
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

import static io.github.bumblesoftware.fastload.config.init.FLMath.*;
import static io.github.bumblesoftware.fastload.init.Fastload.LOGGER;
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

@Restriction(require = @Condition(value = "minecraft", versionPredicates = "1.18.2"))
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Redirect(method = "startIntegratedServer(Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Function;" +
            "ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V", at = @At(value = "INVOKE", target = "Lnet" +
            "/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 2))
    private void remove441(MinecraftClient client, @Nullable Screen screen) {
        var isPreRenderEnabled = isPreRenderEnabled();
        if (isDebugEnabled()) {
            LOGGER.info("isPreRenderEnabled: " + isPreRenderEnabled);
            LOGGER.info("renderChunkRadius: " + FLMath.getRenderChunkRadius());
            LOGGER.info("Fastload Perceived Render Distance: " + ABSTRACTED_CLIENT.getRenderDistance());
        }
        if (isPreRenderEnabled) {
            client.setScreen(ABSTRACTED_CLIENT.newBuildingTerrainScreen());
            if (isDebugEnabled()) {
                LOGGER.info("DownloadingTerrainScreen -> BuildingTerrainScreen");
                LOGGER.info("Goal (Loaded Chunks): " + getPreRenderArea());
            }
        }
        else client.setScreen(new DownloadingTerrainScreen());
    }

    @Redirect(method = "startIntegratedServer(Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Function;" +
            "ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/integrated/IntegratedServer;isLoading()Z"))
    private boolean removeWait(IntegratedServer integratedServer) {
        return true;
    }

    @Redirect(method = "joinWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void removeProgressScreen(MinecraftClient client, Screen screen) {}
}
