package io.github.bumblesoftware.fastload.config.init;

import io.github.bumblesoftware.fastload.util.MinMaxHolder;
import net.minecraft.client.MinecraftClient;

import java.util.function.Supplier;

import static io.github.bumblesoftware.fastload.config.init.FLConfig.*;

public class FLMath {

    //Constants
    private static final double PI = 3.14159265358979323846;


    //Unchanged Constant Getters
    public static int getChunkTryLimit() {
        return parseMinMax(FLConfig.getChunkTryLimit(), DefaultConfig.getTryLimitBound());
    }
    public static Boolean getDebug() {
        return getRawDebug();
    }
    public static int getRadiusBoundMax() {
        return DefaultConfig.getRawRadiusBound().max();
    }
    @SuppressWarnings("unused")
    public static MinMaxHolder getRadiusBound() {
        return DefaultConfig.getRawRadiusBound();
    }
    public static MinMaxHolder getChunkTryLimitBound() {
        return DefaultConfig.getTryLimitBound();
    }


    //Parsing
    private static final Supplier<Double> RENDER_DISTANCE = () ->
            MinecraftClient.getInstance().worldRenderer != null ?
                    Math.min(MinecraftClient.getInstance().worldRenderer.getViewDistance(), getRadiusBoundMax())
                    : getRadiusBoundMax();
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
        return PI * radius * radius;
    }


    //Radii
    public static Integer getPreRenderRadius() {
        return parseMinMax(getRawPreRenderRadius(), Math.min(getRenderDistance(), getRadiusBoundMax()), 0);
    }
    public static Integer getPreRenderRadius(boolean raw) {
        if (raw) return Math.max(getRawPreRenderRadius(), getRadiusBound().min());
        else return getPreRenderRadius();
    }
    public static int getPregenRadius(boolean raw) {
        if (raw) {
            return parseMinMax(getRawChunkPregenRadius(), getRadiusBound());
        }
        return parseMinMax(getRawChunkPregenRadius(), getRadiusBound()) + 1;
    }
    public static int getPregenRadius() {
        return getPregenRadius(true);
    }


    //Areas
    public static int getPregenArea() {
        return getSquareArea(false, parseMinMax(getPregenRadius(), getRadiusBound().max(), getRadiusBound().min()), false);
   }
    public static int getProgressArea() {
        return getSquareArea(true, parseMinMax(getPregenRadius(), getRadiusBound().max(), getRadiusBound().min()), false);
    }
    public static Integer getPreRenderArea() {
        return getCircleArea(getPreRenderRadius()).intValue();
    }


    //Booleans
    public static Boolean getCloseUnsafe() {
        return getCloseLoadingScreenUnsafely();
    }
    public static Boolean getCloseSafe() {
        return getPreRenderRadius() > 0;
    }
    public static Boolean getForceBuild() {
        return getChunkTryLimit() >= 1000;
    }
}
