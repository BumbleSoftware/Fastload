package io.github.bumblesoftware.fastload.mixin.mixins.mc1182.client;

import io.github.bumblesoftware.fastload.util.obj_holders.MutableObjectHolder;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.WORLD_ICON;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.INTEGER_EVENT;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyConstant(method = "updateWorldIcon(Ljava/nio/file/Path;)V", constant = @Constant(intValue = 10))
    private static int delayWorldIcon(int constant) {
        final var returnValue = new MutableObjectHolder<>(constant);
        if (INTEGER_EVENT.isNotEmpty(WORLD_ICON))
            INTEGER_EVENT.execute(List.of(WORLD_ICON), true, returnValue);
        return returnValue.getHeldObj();
    }
}
