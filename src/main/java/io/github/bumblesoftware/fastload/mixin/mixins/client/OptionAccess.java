package io.github.bumblesoftware.fastload.mixin.mixins.client;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Restriction(require = @Condition(value = "minecraft", versionPredicates = "1.18.2"))
@Mixin(Option.class)
public interface OptionAccess {
    @Invoker(value = "getGenericLabel") Text getGenericLabelProxy(Text value);
}
