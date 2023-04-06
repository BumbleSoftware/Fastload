package io.github.bumblesoftware.fastload.mixin.mixins.local;

import io.github.bumblesoftware.fastload.abstraction.client.Client1193;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.BuiltinAbstractionMappings;
import io.github.bumblesoftware.fastload.init.Fastload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.bumblesoftware.fastload.abstraction.tool.AbstractionEvents.CLIENT_ABSTRACTION_EVENT;
import static io.github.bumblesoftware.fastload.util.MinecraftVersionUtil.matchesAny;

@Mixin(BuiltinAbstractionMappings.class)
public class HookClient1193 {
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "register", at = @At("HEAD"), remap = false)
    private static void register1193(CallbackInfo ci) {
        CLIENT_ABSTRACTION_EVENT.registerThreadUnsafe(2, (eventContext, event, closer, eventArgs) -> {
            if (matchesAny("1.19.3")) {
                if (FLMath.isDebugEnabled())
                    Fastload.LOGGER.info("Fastload 1.19.3 Hook!");
                eventContext.clientCalls = new Client1193();
            }
            return null;
        });
    }
}
