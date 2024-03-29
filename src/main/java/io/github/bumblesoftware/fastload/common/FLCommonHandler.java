package io.github.bumblesoftware.fastload.common;

import java.util.List;

import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.INTEGER_EVENT;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.PROGRESS_LISTENER_EVENT;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Locations.PREPARE_START_REGION;

public class FLCommonHandler {
    public static void init() {}

    static {
        INTEGER_EVENT.registerStatic(1, List.of(PREPARE_START_REGION),
                (eventContext, eventStatus, event, eventArgs) -> eventContext.setHeldObj(1)
        );

        PROGRESS_LISTENER_EVENT.registerStatic(1,
                (eventContext, eventStatus, event, eventArgs) -> eventContext.progressListener().stop()
        );
    }
}
