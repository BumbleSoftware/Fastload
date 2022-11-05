package io.github.bumblesoftware.fastload.config;

import net.minecraft.client.MinecraftClient;

import java.util.function.Supplier;

import static io.github.bumblesoftware.fastload.config.FLConfig.*;

public class FLMath {

    //Constants
    private static final int RENDER_RADIUS_BOUND = 32;
    private static final double PI = 3.14159265358979323846;
    private static final int PREGEN_RADIUS = parseMinMax(RAW_CHUNK_PREGEN_RADIUS, 21);



    //Parsing
    private static final Supplier<Double> RENDER_DISTANCE = () ->
            MinecraftClient.getInstance().worldRenderer != null ?
                    Math.min(MinecraftClient.getInstance().worldRenderer.getViewDistance(), RENDER_RADIUS_BOUND)
                    : RENDER_RADIUS_BOUND;
    private static int getRenderDistance() {
        return RENDER_DISTANCE.get().intValue();
    }
    private static int parseMinMax(int toProcess, int max) {
        return Math.max(Math.min(toProcess, max), 0);
    }
    public static int getPregenRadius(boolean raw) {
        if (PREGEN_RADIUS == 0) {
            return 1;
        }
        if (raw) {
            return PREGEN_RADIUS;
        }
        return PREGEN_RADIUS + 1;
    }



    //Calculations
    private static int getSquareArea(boolean worldProgressTracker, int toCalc, @SuppressWarnings("SameParameterValue") boolean raw) {
        int i = toCalc * 2;
        if (!raw) {
            i++;
        }
        if (worldProgressTracker) {
            i ++;
            i ++;
        }
        return i * i;
    }
    public static int getSquareArea(boolean worldProgressTracker, int toCalc) {
        return getSquareArea(worldProgressTracker, toCalc, false);
    }
    public static Double getCircleArea(int radius) {
        return PI * radius * radius;
    }



    //Radii
    public static int getPregenRadius() {
        return getPregenRadius(true);
    }
    public static Integer getPreRenderRadius() {
        return parseMinMax(RAW_PRE_RENDER_RADIUS, getRenderDistance());
    }
    public static Integer getPreRenderRadius(boolean raw) {
        if (raw) return RAW_PRE_RENDER_RADIUS;
        else return getPreRenderRadius();
    }
    public static int getRenderRadiusBound() {
        return RENDER_RADIUS_BOUND;
    }



    //Areas
    public static int getPregenArea() {
        return getSquareArea(false, PREGEN_RADIUS);
   }
    public static Integer getPreRenderArea() {
        int i = getPreRenderRadius() / 2;
        return getCircleArea(getPreRenderRadius()).intValue() - i * i;
    }



    //Booleans
    public static Boolean getCloseUnsafe() {
        return CLOSE_LOADING_SCREEN_UNSAFELY;
    }
    public static Boolean getCloseSafe() {
        return getPreRenderRadius() > 0;
    }
}
