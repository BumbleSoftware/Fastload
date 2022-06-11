package io.github.bumblesoftware.fastload.mixin;

import net.minecraft.client.MinecraftClient;

import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;tick()V"), argsOnly = true)
    public final boolean render(boolean tick) {return true;}

    @Shadow
    public void setScreen(@Nullable Screen screen) {}

    @Inject(at = @At("HEAD"), method = "setScreen", cancellable = true)
    public void setScreen(final Screen screen, final CallbackInfo ci) throws InterruptedException {
        if (screen instanceof DownloadingTerrainScreen) {
            Thread.sleep(400);
            ci.cancel();
            setScreen(null);
        }
    }
}
