package io.github.bumblesoftware.fastload.mixin.local;

import io.github.bumblesoftware.fastload.abstraction.client.Client119;
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
public class HookClient1192 {
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "register", at = @At("HEAD"), remap = false)
    private static void register1192(CallbackInfo ci) {
        CLIENT_ABSTRACTION_EVENT.registerThreadUnsafe(1, (eventContext, event, closer, eventArgs) -> {
            if (matchesAny("1.19", "1.19.1", "1.19.2")) {
                if (FLMath.isDebugEnabled())
                    Fastload.LOGGER.info("Fastload 1.19.0-1-2 Hook!");
                eventContext.clientCalls = new Client119();
            }
            return null;
        });
    }
}
