package com.abdelaziz.fastload.config;

import net.minecraft.client.MinecraftClient;

import java.util.function.Supplier;

import static com.abdelaziz.fastload.config.FLConfig.*;

public class FLMath {
    //Constants
    private static final int RENDER_RADIUS_BOUND = 32;
    private static final int PREGEN_RADIUS_BOUND = 32;
    private static final double PI = 3.14159265358979323846;

    //Parsed Constants
    private static final int PARSED_PREGEN_RADIUS = parseMinMax(RAW_CHUNK_PREGEN_RADIUS, PREGEN_RADIUS_BOUND, 0);
    //RENDER_RADIUS// Cannot be parsed as parsing dynamically changes. No point in making it a field
    private static final int PARSED_CHUNK_TRY_LIMIT = Math.max(RAW_CHUNK_TRY_LIMIT, 1);

    //Unchanged Constant Getters
    public static int getChunkTryLimit() {
        return PARSED_CHUNK_TRY_LIMIT;
    }
    public static Boolean getDebug() {
        return DEBUG;
    }

    //Parsing
    private static final Supplier<Double> RENDER_DISTANCE = () ->
            MinecraftClient.getInstance().worldRenderer != null ?
                    Math.min(MinecraftClient.getInstance().worldRenderer.getViewDistance(), RENDER_RADIUS_BOUND)
                    : RENDER_RADIUS_BOUND;
    private static int getRenderDistance() {
        return RENDER_DISTANCE.get().intValue();
    }
    private static int parseMinMax(int toProcess, int max, @SuppressWarnings("SameParameterValue") int min) {
        return Math.max(Math.min(toProcess, max), min);
    }
    public static int getPregenRadius(boolean raw) {
        if (PARSED_PREGEN_RADIUS == 0) {
            return 1;
        }
        if (raw) {
            return PARSED_PREGEN_RADIUS;
        }
        return PARSED_PREGEN_RADIUS + 1;
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
        return i * i;
    }
    public static Double getCircleArea(int radius) {
        return PI * radius * radius;
    }

    //Radii
    public static Integer getPreRenderRadius() {
        return parseMinMax(RAW_PRE_RENDER_RADIUS, Math.min(getRenderDistance(), RENDER_RADIUS_BOUND), 0);
    }
    public static Integer getPreRenderRadius(boolean raw) {
        if (raw) return Math.max(RAW_PRE_RENDER_RADIUS, 0);
        else return getPreRenderRadius();
    }

    //Areas
    public static int getPregenArea() {
        return getSquareArea(false, PARSED_PREGEN_RADIUS, false);
    }
    public static int getProgressArea() {
        return getSquareArea(true, PARSED_PREGEN_RADIUS, false);
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