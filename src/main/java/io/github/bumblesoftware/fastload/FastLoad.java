package io.github.bumblesoftware.fastload;

import io.github.bumblesoftware.fastload.config.FLConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FastLoad implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Fastload");

	@Override
	public void onInitialize() {
		FLConfig.loadClass();
		LOGGER.warn("Fastload may cause issues, report them to us instead of other mod authors");
		LOGGER.info("SPAWN_CHUNK_RADIUS: " + FLMath.spawnChunkRadius());
		LOGGER.info("SPAWN CHUNK AREA: " + FLMath.getSpawnChunkArea(0));
		LOGGER.info("CANCEL_LOADING_SCREEN: " + FLConfig.CLOSE_LOADING_SCREEN_UNSAFELY);
	}
}
