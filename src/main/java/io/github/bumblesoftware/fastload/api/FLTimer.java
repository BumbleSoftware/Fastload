package io.github.bumblesoftware.fastload.api;

import io.github.bumblesoftware.fastload.api.events.AbstractEvent;
import io.github.bumblesoftware.fastload.client.FLClientHandler;
import io.github.bumblesoftware.fastload.events.FLEvents;

import static io.github.bumblesoftware.fastload.config.init.FLMath.getDebug;

/**
 * A simple timer that automagically registers upon creation, and once ticked down to 0, it de-registers and holds the value.
 * To use this. Make a field with this as its type, initialise it at a specific point, and then use if() statements
 * to compare your desired value & the value of getTime(). Alternatively, you can use the isReady(). Multiply your
 * time value by 20 to get the amount of seconds you want.
 */
public class FLTimer implements AbstractEvent.EventArgs<FLEvents.RecordTypes.RenderTickEventContext> {
    private int time;

    /**
     * Sets time & registers the event
     */
    public FLTimer(int time) {
        if (time < 0) time = 0;
        this.time = time;
        registerEvent();
    }

    /**
     * Getter for the remaining time.
     */
    public int getTime() {
        return time;
    }

    /**
     * returns true until timer is finished
     */
    public boolean isReady() {
        return time > 0;
    }

    /**
     * Custom impl of args so that this object is an instance of AbstractEvent, allowing it to be referred using the 'this' keyword.
     * Makes it easier to remove the event at any desired moment.
     */
    @Override
    public void args(FLEvents.RecordTypes.RenderTickEventContext eventContext) {
        if (this.time == 0) {
            FLEvents.RENDER_TICK_EVENT.removeEvent(this);
        }
        if (this.time > 0) {
            this.time--;
            if (getDebug()) FLClientHandler.log("" + this.time);
        }
    }

    /**
     * Registers this timer to the render tick event.
     */
    private void registerEvent() {
        if (time > 0) {
            FLEvents.RENDER_TICK_EVENT.register(this);
        }

    }
}