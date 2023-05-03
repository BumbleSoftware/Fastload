package io.github.bumblesoftware.fastload.common;

import java.util.List;

import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.INTEGER_EVENT;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.PROGRESS_LISTENER_EVENT;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Locations.PREPARE_START_REGION;

public class FLCommonHandler {
    @SuppressWarnings("EmptyMethod")
    public static void init() {}

    static {
        INTEGER_EVENT.registerThreadUnsafe(1, List.of(PREPARE_START_REGION),
                (eventContext,event, eventArgs) -> eventContext.heldObj = 1
        );

        PROGRESS_LISTENER_EVENT.registerThreadUnsafe(1,
                (eventContext,event, eventArgs) -> eventContext.progressListener().stop()
        );
    }
}
