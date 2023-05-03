package io.github.bumblesoftware.fastload.util;

import io.github.bumblesoftware.fastload.init.Fastload;

import java.util.List;

import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.BOOLEAN_EVENT;
import static io.github.bumblesoftware.fastload.config.FLMath.isDebugEnabled;

/**
 * Simple event-based timer with respect to minecraft ticks.
 * To use, simply set your value via setTime(), then use isReady() for your if() checks.
 * The timer will bool down your value to 0. Each second is worth 20 ticks. Which means
 * while your value isn't 0, isReady() will return true. Negative numbers
 */
public final class TickTimer {
    /**
     * Stores the remaining time for the timer.
     */
    private int remainingTime = 0;

    /**
     * Registers a client event for the timer to use
     */
    public TickTimer(final String location) {
        BOOLEAN_EVENT.registerThreadUnsafe(1, List.of(location),
                (eventContext,event, eventArgs) -> {
                    if (remainingTime > 0) {
                        remainingTime--;
                        if (isDebugEnabled()) Fastload.LOGGER.info(String.valueOf(remainingTime));
                    }
                }
        );
    }


    /**
     * @return true if timer hasn't depleted it's {@link #remainingTime}
     */
    public boolean isReady() {
        return remainingTime > 0;
    }

    /**
     * @param remainingTime sets amount of time in ticks
     */
    public void setTime(int remainingTime) {
        if (remainingTime < 0) {
            throw new RuntimeException("FLTimer was called with a negative number!");
        }
        this.remainingTime = remainingTime;
    }

    /**
     * @param remainingTime sets amount of time in ticks
     * @param inSeconds makes remainingTime measured in seconds rather than ticks.
     */
    @SuppressWarnings("unused")
    public void setTime(int remainingTime, final boolean inSeconds) {
        if (inSeconds) {
            remainingTime = remainingTime * 20;
        }
        setTime(remainingTime);
    }


    /**
     * @return the amount of remaining time.
     */
    public int getTime() {
        return remainingTime;
    }
}