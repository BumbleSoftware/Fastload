package io.github.bumblesoftware.fastload.mixin.local;

import io.github.bumblesoftware.fastload.abstraction.AbstractClientCalls;
import io.github.bumblesoftware.fastload.abstraction.client119.Client119;
import io.github.bumblesoftware.fastload.init.FastloadClient;
import net.minecraft.MinecraftVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FastloadClient.class)
public class FastloadClientMixin {
    @Inject(method = "getAbstractedClient", at = @At("HEAD"), remap = false)
    private static void compat119(CallbackInfoReturnable<AbstractClientCalls> cir) {
        if (MinecraftVersion.CURRENT.getName().equals("1.19"))
            cir.setReturnValue(new Client119());
    }
}
