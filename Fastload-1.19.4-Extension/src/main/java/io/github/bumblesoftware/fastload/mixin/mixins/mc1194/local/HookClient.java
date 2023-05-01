package io.github.bumblesoftware.fastload.mixin.mixins.mc1194.local;

import io.github.bumblesoftware.fastload.abstraction.client.Client1194;
import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.init.FastloadClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.bumblesoftware.fastload.init.FastloadClient.CLIENT_ABSTRACTION_EVENT;
import static io.github.bumblesoftware.fastload.api.external.abstraction.tool.version.VersionConstants.IS_MINECRAFT_1194;
import static io.github.bumblesoftware.fastload.api.external.abstraction.tool.version.VersionConstants.IS_MINECRAFT_1200;

@Mixin(FastloadClient.class)
public class HookClient {
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "registerBaseClient", at = @At("HEAD"), remap = false)
    private static void register1194(CallbackInfo ci) {
        CLIENT_ABSTRACTION_EVENT.registerThreadUnsafe(3,
                event -> event.stableArgs((eventContext, eventArgs) -> {
                    if (IS_MINECRAFT_1194 || IS_MINECRAFT_1200) {
                        if (FLMath.isDebugEnabled())
                            Fastload.LOGGER.info("Fastload 1.19.4 Hook!");
                        eventContext.clientCallsHolder().heldObj = new Client1194();
                    }
                })
        );
    }
}
