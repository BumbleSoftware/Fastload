package io.github.bumblesoftware.fastload;

import io.github.bumblesoftware.fastload.config.FLConfig;

public class FLMath {
   public static final int ORIGINAL_SPAWN_CHUNK_RADIUS = FLConfig.CHUNK_PREGEN_RADIUS;
    public static final int SET_SPAWN_CHUNK_RADIUS = spawnChunkRadius();
   public static int spawnChunkRadius() {
       if (ORIGINAL_SPAWN_CHUNK_RADIUS > 20) {
           return  20;
       } else return Math.max(ORIGINAL_SPAWN_CHUNK_RADIUS, 1);
   }
   public static int getSetSpawnChunkRadius() {
       return SET_SPAWN_CHUNK_RADIUS + 1;
   }

    public static int getSpawnChunkArea(int add) {
        int i = (SET_SPAWN_CHUNK_RADIUS + add) * 2 + 1;
        return i * i;
    }

}
