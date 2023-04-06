package io.github.bumblesoftware.fastload.config.init;


import io.github.bumblesoftware.fastload.util.MinMaxHolder;

public interface DefaultConfig {
    String CHUNK_TRY_LIMIT_KEY = "chunk_try_limit";
    String DEBUG_KEY = "debug";
    String RENDER_RADIUS_KEY = "render_chunk_radius";
    String RENDER_AREA_KEY = "render_chunk_area";
    String SERVER_RENDER_DIVISOR_KEY = "server_render_divisor";


    MinMaxHolder CHUNK_RADIUS_BOUND = new MinMaxHolder(32, 0);
    MinMaxHolder CHUNK_TRY_LIMIT_BOUND = new MinMaxHolder(1000, 1);
    MinMaxHolder SERVER_RENDER_DIVISOR_BOUND = new MinMaxHolder(10, 0);

    boolean DEF_DEBUG_VALUE = false;
    int DEF_SERVER_RENDER_DIVISOR = 1;
    int DEF_RENDER_RADIUS_VALUE = 10;
    int DEF_TRY_LIMIT_VALUE = 100;
}
