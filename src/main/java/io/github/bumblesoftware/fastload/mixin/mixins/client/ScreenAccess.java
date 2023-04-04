package io.github.bumblesoftware.fastload.mixin.mixins.client;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Restriction(require = @Condition(value = "minecraft", versionPredicates = "1.18.2"))
@Mixin(Screen.class)
public interface ScreenAccess {
    @Invoker(value = "addDrawableChild")  <T extends Element & Drawable> T addDrawableChildProxy(T value);
}
