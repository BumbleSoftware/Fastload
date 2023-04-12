package io.github.bumblesoftware.fastload.abstraction.tool;

import io.github.bumblesoftware.fastload.abstraction.client.AbstractClientCalls;

public class AbstractedClientHolder {
    public AbstractClientCalls clientCalls;

    public AbstractedClientHolder(AbstractClientCalls clientCalls) {
        this.clientCalls = clientCalls;
    }
}