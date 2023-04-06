package io.github.bumblesoftware.fastload.config.init;

import io.github.bumblesoftware.fastload.util.MinMaxHolder;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.CHUNK_RADIUS_BOUND;
import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.CHUNK_TRY_LIMIT_BOUND;
import static io.github.bumblesoftware.fastload.config.init.FLConfig.*;
import static io.github.bumblesoftware.fastload.init.FastloadClient.ABSTRACTED_CLIENT;

public class FLMath {

    public static int getChunkTryLimit() {
        return parseMinMax(FLConfig.getRawChunkTryLimit(), CHUNK_TRY_LIMIT_BOUND);
    }


    protected static int parseMinMax(int toProcess, int max, int min) {
        return Math.max(Math.min(toProcess, max), min);
    }

    protected static int parseMinMax(int toProcess, MinMaxHolder minMaxHolder) {
        return parseMinMax(toProcess, minMaxHolder.max(), minMaxHolder.min());
    }

    public static double getCircleArea(int radius) {
        return Math.PI * radius * radius;
    }

    public static int getRenderChunkRadius() {
        return parseMinMax(
                getRawRenderRadius(),
                Math.min(ABSTRACTED_CLIENT.getRenderDistance(),
                        CHUNK_RADIUS_BOUND.max()
                ),
                0
        );
    }
    public static int getRenderChunkRadius(boolean raw) {
        if (raw)
            return Math.max(
                    getRawRenderRadius(),
                    CHUNK_RADIUS_BOUND.min()
            );
        else
            return getRenderChunkRadius();
    }
    public static int getPreRenderArea() {
        return (int)getCircleArea(getRenderChunkRadius());
    }
    public static int getServerRenderDivisor() {
        return getRawServerRenderDivisor();
    }
    public static Boolean isDebugEnabled() {
        return getRawDebug();
    }
    public static Boolean isServerRenderEnabled() {
        return getServerRenderDivisor() > 0;
    }
    public static Boolean isPreRenderEnabled() {
        return getRenderChunkRadius() > 0;
    }
}
