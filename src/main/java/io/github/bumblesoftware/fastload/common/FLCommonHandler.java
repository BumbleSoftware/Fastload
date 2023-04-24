package io.github.bumblesoftware.fastload.common;

import java.util.List;

import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.INTEGER_EVENT;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.PROGRESS_LISTENER_EVENT;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Locations.PREPARE_START_REGION;

public class FLCommonHandler {
    static {
        INTEGER_EVENT.registerThreadUnsafe(1, List.of(PREPARE_START_REGION),
                event -> event.stableArgs((eventContext, eventArgs) -> eventContext.heldObj = 1)
        );

        PROGRESS_LISTENER_EVENT.registerThreadUnsafe(1,
                event -> event.stableArgs((eventContext, eventArgs) -> eventContext.progressListener().stop())
        );
    }

    @SuppressWarnings("EmptyMethod")
    public static void init() {
    }
}
