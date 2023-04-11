package io.github.bumblesoftware.fastload.mixin.mixins.mc1182.client;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface ScreenAccess {
    @Invoker(value = "addDrawableChild")  <T extends Element & Drawable> T addDrawableChildProxy(T value);
}
