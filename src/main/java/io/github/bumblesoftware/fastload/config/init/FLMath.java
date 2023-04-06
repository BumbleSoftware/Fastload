package io.github.bumblesoftware.fastload.config.init;

import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.MinMaxHolder;
import net.minecraft.client.MinecraftClient;

import java.util.function.Supplier;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.config.init.FLConfig.*;

public class FLMath {

    public static int getChunkTryLimit() {
        return parseMinMax(FLConfig.getChunkTryLimit(), TRY_LIMIT_BOUND);
    }
    public static int getRadiusBoundMax() {
        return CHUNK_RADIUS_BOUND.max();
    }
    @SuppressWarnings("unused")
    public static MinMaxHolder getRadiusBound() {
        return CHUNK_RADIUS_BOUND;
    }
    public static MinMaxHolder getChunkTryLimitBound() {
        return TRY_LIMIT_BOUND;
    }


    private static final Supplier<Double> RENDER_DISTANCE = () ->
            MinecraftClient.getInstance().worldRenderer != null ?
                    Math.min(
                            MinecraftClient.getInstance().worldRenderer.getViewDistance(),
                            getRadiusBoundMax()
                    ) : getRadiusBoundMax();
    private static int getRenderDistance() {
        return RENDER_DISTANCE.get().intValue();
    }
    protected static int parseMinMax(int toProcess, int max, @SuppressWarnings("SameParameterValue") int min) {
        return Math.max(Math.min(toProcess, max), min);
    }
    protected static int parseMinMax(int toProcess, MinMaxHolder maxMin) {
        return Math.max(Math.min(toProcess, maxMin.max()), maxMin.min());
    }

    public static double getCircleArea(int radius) {
        return Math.PI * radius * radius;
    }


    public static int getRenderChunkRadius() {
        return parseMinMax(
                getRawPreRenderRadius(),
                Math.min(getRenderDistance(),
                        getRadiusBoundMax()
                ),
                0
        );
    }
    public static int getRenderChunkRadius(boolean raw) {
        if (raw)
            return Math.max(
                    getRawPreRenderRadius(),
                    getRadiusBound().min()
            );
        else
            return getRenderChunkRadius();
    }
    public static int getPreRenderArea() {
        return (int)getCircleArea(getRenderChunkRadius());
    }

    public static Boolean isDebugEnabled() {
        return getRawDebug();
    }
    public static Boolean isForceBuildEnabled() {
        return getChunkTryLimit() >= 1000;
    }
    public static Boolean isPreRenderEnabled() {
        var renderChunkRadius = getRenderChunkRadius();
        if (isDebugEnabled())
            Fastload.LOGGER.info("renderChunkRadius = " + renderChunkRadius);
        return renderChunkRadius > 0;
    }
}
