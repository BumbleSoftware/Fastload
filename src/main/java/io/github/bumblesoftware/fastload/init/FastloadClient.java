package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.client.FLClientHandler;
import io.github.bumblesoftware.fastload.events.FLClientEvents;
import net.fabricmc.api.ClientModInitializer;

public class FastloadClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FLClientEvents.init();
        FLClientHandler.init();
    }
}
