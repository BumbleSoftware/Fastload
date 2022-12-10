package io.github.bumblesoftware.fastload.client;

import io.github.bumblesoftware.fastload.api.events.SetScreenEvent;

public class ClientHandler {
    public static void initClass() {}

    static {
        SetScreenEvent.register((screen, ci) -> {
        });
    }
}
