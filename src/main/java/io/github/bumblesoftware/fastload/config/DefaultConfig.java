package io.github.bumblesoftware.fastload.config;


import io.github.bumblesoftware.fastload.util.Bound;

public interface DefaultConfig {
    String CHUNK_TRY_LIMIT_KEY = "chunk_try_limit";
    String DEBUG_KEY = "debug";
    String INSTANT_LOAD_KEY = "instant_load";
    String LOCAL_RENDER_KEY = renderKey("local");
    String LOCAL_RENDER_RADIUS_KEY = suffix(LOCAL_RENDER_KEY, false);
    String LOCAL_RENDER_AREA_KEY = suffix(LOCAL_RENDER_KEY, true);
    String SERVER_RENDER_KEY = renderKey("server");
    String SERVER_RENDER_RADIUS_KEY = suffix(SERVER_RENDER_KEY, false);
    String SERVER_RENDER_AREA_KEY = suffix(SERVER_RENDER_KEY, true);

    Bound LOCAL_CHUNK_RADIUS_BOUND = new Bound(32, 0);
    Bound SERVER_CHUNK_RADIUS_BOUND = new Bound(LOCAL_CHUNK_RADIUS_BOUND.max()/2, LOCAL_CHUNK_RADIUS_BOUND.min());
    Bound CHUNK_TRY_LIMIT_BOUND = new Bound(2500, 1);

    boolean DEF_DEBUG_VALUE = false;
    boolean DEF_INSTANT_LOAD_VALUE = false;
    int DEF_SERVER_RENDER_RADIUS_VALUE = 3;
    int DEF_RENDER_RADIUS_VALUE = 10;
    int DEF_TRY_LIMIT_VALUE = 250;


    private static String renderKey(final String env) {
        return env + "_render";
    }

    private static String suffix(final String renderType, final boolean area) {
        if (!area)
            return renderType + "_chunk_radius";
        return renderType + "_chunk_area";
    }
}
