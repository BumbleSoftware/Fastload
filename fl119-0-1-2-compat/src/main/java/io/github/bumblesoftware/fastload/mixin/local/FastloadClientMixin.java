package io.github.bumblesoftware.fastload.mixin.local;

import io.github.bumblesoftware.fastload.abstraction.AbstractClientCalls;
import io.github.bumblesoftware.fastload.abstraction.client119.Abstraction119Extension;
import io.github.bumblesoftware.fastload.abstraction.client119.Client119;
import io.github.bumblesoftware.fastload.init.FastloadClient;
import net.minecraft.MinecraftVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(FastloadClient.class)
public class FastloadClientMixin {
    @Inject(method = "getAbstractedClient", at = @At("HEAD"), remap = false, cancellable = true)
    private static void compat119(CallbackInfoReturnable<AbstractClientCalls> cir) {
        for (String version : List.of(
                "1.19",
                "1.19.1",
                "1.19.2"
        )) {
            if (MinecraftVersion.CURRENT.getName().equals(version))
                cir.setReturnValue(new Client119());
            else {
                final var extension = Abstraction119Extension.extend119Abstraction();
                if (extension != null) {
                    cir.setReturnValue(extension);
                }
            }
        }
    }
}
