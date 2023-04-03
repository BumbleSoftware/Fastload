package io.github.bumblesoftware.fastload.config.init;

import io.github.bumblesoftware.fastload.util.MinMaxHolder;
import net.minecraft.client.MinecraftClient;

import java.util.function.Supplier;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.config.init.FLConfig.*;

public class FLMath {

    //Unchanged Constant Getters
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


    //Parsing
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


    //Calculations
    @SuppressWarnings("SameParameterValue")
    private static int getSquareArea(boolean worldProgressTracker, int toCalc, boolean raw) {
        int i = toCalc * 2;
        if (!raw) {
            i++;
        }
        if (worldProgressTracker) {
            i ++;
            i ++;
        }
        if (i == 0) {
            i = 1;
        }
        return i * i;
    }
    public static Double getCircleArea(int radius) {
        return Math.PI * radius * radius;
    }


    //Radii
    public static Integer getPreRenderRadius() {
        return parseMinMax(
                getRawPreRenderRadius(),
                Math.min(getRenderDistance(),
                        getRadiusBoundMax()
                ),
                0
        );
    }
    public static Integer getPreRenderRadius(boolean raw) {
        if (raw)
            return Math.max(
                    getRawPreRenderRadius(),
                    getRadiusBound().min()
            );
        else
            return getPreRenderRadius();
    }
    public static int getPregenRadius(boolean raw) {
        if (raw)
            return parseMinMax(
                    getRawChunkPregenRadius(),
                    getRadiusBound()
            );
        else
            return parseMinMax(
                    getRawChunkPregenRadius(),
                    getRadiusBound()
            ) + 1;
    }
    public static int getPregenRadius() {
        return getPregenRadius(true);
    }


    //Areas
    public static int getPregenArea() {
        return getSquareArea(
                false,
                parseMinMax(
                        getPregenRadius(),
                        getRadiusBound().max(),
                        getRadiusBound().min()
                ),
                false
        );
   }
    public static int getProgressArea() {
        return getSquareArea(
                true,
                parseMinMax(
                        getPregenRadius(),
                        getRadiusBound().max(),
                        getRadiusBound().min()
                ),
                false
        );
    }
    public static Integer getPreRenderArea() {
        return getCircleArea(getPreRenderRadius()).intValue();
    }


    //Booleans
    public static Boolean isDebugEnabled() {
        return getRawDebug();
    }
    public static Boolean isForceBuildEnabled() {
        return getChunkTryLimit() >= 1000;
    }
    public static Boolean isForceCloseEnabled() {
        return getCloseLoadingScreenUnsafely();
    }
    public static Boolean isPreRenderEnabled() {
        return getPreRenderRadius() > 0;
    }
}
