package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.abstraction.AbstractClientCalls;
import io.github.bumblesoftware.fastload.abstraction.client1182.Client1182;
import io.github.bumblesoftware.fastload.client.FLClientEvents;
import io.github.bumblesoftware.fastload.client.FLClientHandler;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.MinecraftVersion;

public class FastloadClient implements ClientModInitializer {
    public static final AbstractClientCalls ABSTRACTED_CLIENT = getAbstractedClient();

    public static AbstractClientCalls getAbstractedClient() {
        var version = MinecraftVersion.CURRENT.getName();
        if (version.equals("1.18.2"))
            return new Client1182();
        else throw new NullPointerException("Method abstraction for MC Client is unsupported for this version");
    }

    @Override
    public void onInitializeClient() {
        FLClientEvents.init();
        FLClientHandler.init();
    }
}