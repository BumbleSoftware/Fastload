package io.github.bumblesoftware.fastload.abstraction.tool;

import io.github.bumblesoftware.fastload.api.events.AbstractEvent;
import io.github.bumblesoftware.fastload.api.events.CapableEvent;

public class AbstractionEvents {
    public static final AbstractEvent<AbstractedClientHolder> CLIENT_ABSTRACTION_EVENT = new CapableEvent<>();
}
