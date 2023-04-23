package io.github.bumblesoftware.fastload.mixin.mixins.mc119.local;

import io.github.bumblesoftware.fastload.abstraction.client.Client119;
import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.init.FastloadClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.bumblesoftware.fastload.init.FastloadClient.CLIENT_ABSTRACTION_EVENT;
import static io.github.bumblesoftware.fastload.util.MinecraftVersionUtil.EQUALS;
import static io.github.bumblesoftware.fastload.util.MinecraftVersionUtil.matchesAny;

@Mixin(FastloadClient.class)
public class HookClient {
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "registerBaseClient", at = @At("HEAD"), remap = false)
    private static void register1192(CallbackInfo ci) {
        CLIENT_ABSTRACTION_EVENT.registerThreadUnsafe(1,
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (matchesAny(EQUALS,  "1.19.2", "1.19.1", "1.19")) {
                        if (FLMath.isDebugEnabled())
                            Fastload.LOGGER.info("Fastload 1.19.0-1-2 Hook!");
                        eventContext.clientCallsHolder().heldObj = new Client119();
                    }
                })
        );
    }
}
