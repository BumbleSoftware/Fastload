package io.github.bumblesoftware.fastload.config;

import io.github.bumblesoftware.fastload.util.lambda.LambdaIf;

import static io.github.bumblesoftware.fastload.config.DefaultConfig.CHUNK_TRY_LIMIT_BOUND;
import static io.github.bumblesoftware.fastload.config.FLConfig.*;

public class FLMath {
    private static final LambdaIf DEBUG_LAMBDA = LambdaIf.getNew(getRawDebug());

    public static int getChunkTryLimit() {
        return CHUNK_TRY_LIMIT_BOUND.minMax(getRawChunkTryLimit());
    }

    public static LambdaIf isDebugEnabled() {
        return DEBUG_LAMBDA;
    }

    public static void ifDebugEnabled(Runnable runnable) {
        isDebugEnabled().runIf(runnable);
    }

    public static Boolean isInstantLoadEnabled() {
        return getRawInstantLoad();
    }
}
