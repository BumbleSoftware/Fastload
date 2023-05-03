package io.github.bumblesoftware.fastload.mixin.mixins.mc1182.client;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface ClientAccess {
    @Invoker(value = "reset") void resetProxy(final Screen screen);
}
