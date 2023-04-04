package io.github.bumblesoftware.fastload.mixin.mixins.local;

import io.github.bumblesoftware.fastload.abstraction.AbstractClientCalls;
import io.github.bumblesoftware.fastload.abstraction.client1194.Client1194;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.init.FastloadClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.bumblesoftware.fastload.util.MinecraftVersionUtil.compareWithOperator;

@Mixin(FastloadClient.class)
public class Hook1194 {
    @Inject(method = "getAbstractedClient", at = @At("HEAD"), remap = false, cancellable = true)
    private static void compat1194(CallbackInfoReturnable<AbstractClientCalls> cir) {
        if (FLMath.isDebugEnabled())
            Fastload.LOGGER.info("compat1194");
        if (compareWithOperator(">=", "1.19.4"))
            cir.setReturnValue(new Client1194());
    }
}
