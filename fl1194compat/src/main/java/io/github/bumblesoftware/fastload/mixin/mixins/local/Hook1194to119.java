package io.github.bumblesoftware.fastload.mixin.mixins.local;

import io.github.bumblesoftware.fastload.abstraction.AbstractClientCalls;
import io.github.bumblesoftware.fastload.abstraction.client119.Abstraction119Extension;
import io.github.bumblesoftware.fastload.abstraction.client1194.Client1194;
import net.minecraft.MinecraftVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Abstraction119Extension.class)
public class Hook1194to119 {
    @Inject(method = "extend119Abstraction", at = @At("HEAD"), remap = false, cancellable = true)
    private static void compat1194(CallbackInfoReturnable<AbstractClientCalls> cir) {
            if (MinecraftVersion.CURRENT.getName().equals("1.19.4"))
                cir.setReturnValue(new Client1194());
    }
}
