package io.github.bumblesoftware.fastload.config;

import net.minecraft.client.MinecraftClient;

import java.util.function.Supplier;

import static io.github.bumblesoftware.fastload.config.FLConfig.*;

public class FLMath {
    private static final int PREGEN_RADIUS = parseRadius(RAW_CHUNK_PREGEN_RADIUS, 21);
    private static final Supplier<Double> getRenderDistanceLimit = () -> MinecraftClient.getInstance().worldRenderer != null ? MinecraftClient.getInstance().worldRenderer.getViewDistance() : 12;
    private static final int CALCULATED_AREA = getPregenArea(0);
    private static int parseRadius(int toProcess, int max) {
        return Math.max(Math.min(toProcess, max), 0);
    }

    public static int getPregenRadius(boolean raw) {
        if (raw) {
            if (PREGEN_RADIUS == 0) {
                return 1;
            }
            return PREGEN_RADIUS;
        } else return getPregenRadius();
    }
    public static int getPregenRadius() {
        return PREGEN_RADIUS + 1;
    }
    public static int getPregenArea(int worldProgressTracker) {
        int i = (PREGEN_RADIUS + worldProgressTracker) * 2 + 1;
        return i * i;
    }
    public static int getPregenArea() {
        return CALCULATED_AREA;
   }
    public static Boolean getCloseUnsafe() {
        return CLOSE_LOADING_SCREEN_UNSAFELY;
    }
    public static Boolean getCloseSafe() {
        return getPreRenderRadius() > 0;
    }
    public static Integer getPreRenderRadius() {
        return parseRadius(RAW_PRE_RENDER_RADIUS, getRenderDistanceLimit.get().intValue());
    }
}
