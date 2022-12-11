package io.github.bumblesoftware.fastload.util;

import io.github.bumblesoftware.fastload.events.FLEvents;

import static io.github.bumblesoftware.fastload.client.FLClientHandler.log;
import static io.github.bumblesoftware.fastload.config.init.FLMath.getDebug;

/**
 * A simple timer that automagically registers upon creation, and once ticked down to 0, it de-registers and holds the value.
 * To use this. Make a field with this as its type, initialise it at a specific point, and then use if() statements
 * to compare your desired value & the value of getTime(). Alternatively, you can use the isReady(). Multiply your
 * time value by 20 to get the amount of seconds you want.
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
        FLTimer.remainingTime = remainingTime;
    }

    public static int getTime() {
        return remainingTime;
    }
}