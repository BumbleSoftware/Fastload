package io.github.bumblesoftware.fastload.mixin.local;

import io.github.bumblesoftware.fastload.abstraction.AbstractClientCalls;
import io.github.bumblesoftware.fastload.abstraction.client119.Client119;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.init.FastloadClient;
import io.github.bumblesoftware.fastload.util.MinecraftVersionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FastloadClient.class)
public class Hook1192 {
    @Inject(method = "getAbstractedClient", at = @At("HEAD"), remap = false, cancellable = true)
    private static void compat119(CallbackInfoReturnable<AbstractClientCalls> cir) {
        if (FLMath.isDebugEnabled())
            Fastload.LOGGER.info("compat119-0-1-2");
        if (MinecraftVersionUtil.compareAll("1.19", "1.19.1", "1.19.2"))
            cir.setReturnValue(new Client119());
    }
}
