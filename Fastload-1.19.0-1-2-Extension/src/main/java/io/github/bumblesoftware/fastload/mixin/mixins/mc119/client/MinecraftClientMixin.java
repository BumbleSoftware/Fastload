package io.github.bumblesoftware.fastload.mixin.mixins.mc119.client;

import io.github.bumblesoftware.fastload.client.FLClientEvents;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.integrated.IntegratedServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.LLS_441_REDIRECT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.SET_SCREEN_EVENT;

@Restriction(require = @Condition(value = "minecraft", versionPredicates = {
        "1.19",
        "1.19.1",
        "1.19.2"
}))
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Redirect(method = "startIntegratedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void remove441(MinecraftClient client, @Nullable Screen screen) {
        if (SET_SCREEN_EVENT.isNotEmpty())
            SET_SCREEN_EVENT.fireEvent(
                    List.of(LLS_441_REDIRECT),
                    new FLClientEvents.Contexts.SetScreenEventContext(screen, null)
            );
    }

    @Redirect(method = "startIntegratedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/integrated/IntegratedServer;isLoading()Z"))
    private boolean removeWait(IntegratedServer integratedServer) {
        return true;
    }

    @SuppressWarnings("EmptyMethod")
    @Redirect(method = "joinWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void removeProgressScreen(MinecraftClient client, Screen screen) {}
}
