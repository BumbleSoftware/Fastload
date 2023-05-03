package io.github.bumblesoftware.fastload.common;

import io.github.bumblesoftware.fastload.api.external.events.AbstractEvent;
import io.github.bumblesoftware.fastload.api.external.events.CapableEvent;
import io.github.bumblesoftware.fastload.util.MutableObjectHolder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.util.math.ChunkPos;

import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Contexts.*;

/**
 * Stores important events based on {@link AbstractEvent} that fastload uses.
 * Common events.
 */
public interface FLCommonEvents {
    static void init() {}

    interface Events {
        AbstractEvent<MutableObjectHolder<Boolean>> BOOLEAN_EVENT = new CapableEvent<>();
        AbstractEvent<MutableObjectHolder<Integer>> INTEGER_EVENT = new CapableEvent<>();
        AbstractEvent<MutableObjectHolder<Runnable>> RUNNABLE_EVENT = new CapableEvent<>();
        AbstractEvent<EmptyContext> EMPTY_EVENT =  new CapableEvent<>();
        AbstractEvent<ProgressListenerContext> PROGRESS_LISTENER_EVENT = new CapableEvent<>();
        AbstractEvent<ServerContext<Boolean>> SERVER_EVENT = new CapableEvent<>();

    }
    interface Locations {
        String SERVER_TICK = "minecraft_server;server_tick;";
        String PREPARE_START_REGION = "minecraft_server;prepare_start_region;modify_constant_441;";
        String SERVER_PSR_LOADING_REDIRECT = "minecraft_server;prepare_start_region;is_loading;redirect";
    }

    interface Contexts {
        record EmptyContext() {}
        record ServerContext<T>(MinecraftServer server, MutableObjectHolder<T> returnValue) {}
        record ProgressListenerContext(WorldGenerationProgressListener progressListener, ChunkPos chunkPos) {}
    }
}
