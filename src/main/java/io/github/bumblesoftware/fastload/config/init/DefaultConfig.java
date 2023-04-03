package io.github.bumblesoftware.fastload.config.init;


import io.github.bumblesoftware.fastload.util.MinMaxHolder;

public interface DefaultConfig {
    String TRY_LIMIT_KEY = "chunk_try_limit";
    String FORCE_CLOSE_KEY = "close_loading_screen_unsafely";
    String DEBUG_KEY = "debug";
    String PREGEN_RADIUS_KEY = "pregen_chunk_radius";
    String PREGEN_AREA_KEY = "pregen_chunk_area";
    String RENDER_RADIUS_KEY = "render_chunk_radius";
    String RENDER_AREA_KEY = "render_chunk_area";

    MinMaxHolder CHUNK_RADIUS_BOUND = new MinMaxHolder(32, 0);
    MinMaxHolder TRY_LIMIT_BOUND = new MinMaxHolder(1000, 1);
    int DEF_RENDER_RADIUS_VALUE = 0;
    int DEF_PREGEN_RADIUS_VALUE = 5;
    int DEF_TRY_LIMIT_VALUE = 100;
    boolean DEF_FORCE_CLOSE_VALUE = false;
    boolean DEF_DEBUG_VALUE = false;
}
