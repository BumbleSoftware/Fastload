package io.github.bumblesoftware.fastload.util;

import io.github.bumblesoftware.fastload.events.FLEvents;

import static io.github.bumblesoftware.fastload.client.FLClientHandler.log;
import static io.github.bumblesoftware.fastload.config.init.FLMath.getDebug;

/**
 * Simple event-based timer with respect to minecraft client ticks.
 * To use, simply set your value via setTime(), then use isReady() for your if() checks.
 * The timer will tick down your value to 0. Each second is worth 20 ticks. Which means
 * while your value isn't 0, isReady() will return true. Negative numbers
 */
public final class FLTimer {
    private static int remainingTime = 0;
    private FLTimer() {}

    static  {
        FLEvents.RENDER_TICK_EVENT.register((eventContext, abstractParent, eventArgs) -> {
            if (remainingTime > 0) {
                remainingTime--;
                if (getDebug()) log("" + remainingTime);
            }
        }, 1);
    }

    public static boolean isReady() {
        return remainingTime > 0;
    }

    public static void setTime(int remainingTime) {
        if (remainingTime < 0) {
            throw new RuntimeException("FLTimer was called with a negative number!");
        }
        FLTimer.remainingTime = remainingTime;
    }

    public static int getTime() {
        return remainingTime;
    }
}