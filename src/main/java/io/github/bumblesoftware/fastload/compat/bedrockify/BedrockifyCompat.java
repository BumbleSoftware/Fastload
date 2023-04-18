package io.github.bumblesoftware.fastload.compat.bedrockify;

import io.github.bumblesoftware.fastload.abstraction.client.AbstractClientCalls;
import io.github.bumblesoftware.fastload.api.events.AbstractEvent;
import io.github.bumblesoftware.fastload.api.events.CapableEvent;
import io.github.bumblesoftware.fastload.util.ObjectHolder;
import net.minecraft.text.Text;

import java.util.function.Supplier;


public final class BedrockifyCompat {
    private BedrockifyCompat() {}
    public static final AbstractEvent<Context> BEDROCKIFY_COMPAT_EVENT = new CapableEvent<>();

    public record Context(
            ObjectHolder<Boolean> shouldContinueMethodCall,
            AbstractClientCalls clientCalls,
            Text screenName,
            Text screenTemplate,
            Text preparingChunks,
            Text buildingChunks,
            String loadedChunksString,
            String builtChunksString,
            Supplier<Integer> getLoadedChunkCount,
            Supplier<Integer> getBuiltChunkCount,
            int loadingAreaGoal
    ) {}
}
