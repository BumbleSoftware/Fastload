package io.github.bumblesoftware.fastload.api.events;

import io.github.bumblesoftware.fastload.api.events.custom.FLPauseMenuEvent;
import io.github.bumblesoftware.fastload.api.events.custom.FLRenderTickEvent;
import io.github.bumblesoftware.fastload.api.events.custom.FLSetScreenEvent;

public class FLEvents {
    public static final FLSetScreenEvent SET_SCREEN_EVENT = new FLSetScreenEvent();
    public static final FLRenderTickEvent RENDER_TICK_EVENT = new FLRenderTickEvent();
    public static final FLPauseMenuEvent PAUSE_MENU_EVENT = new FLPauseMenuEvent();


}
