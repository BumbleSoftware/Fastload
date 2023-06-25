package io.github.bumblesoftware.fastload.updated_code.event.core;

import java.util.Queue;

public record EventAccessParameters<Context>(
        String location,
        long priority,
        Queue<EventArgs<Context>> args
) { }
