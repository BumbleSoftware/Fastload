package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;

public class Fastload implements ModInitializer {
	public static final String NAMESPACE = "Fastload";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	private static String logKey(String key) {
		return key.toUpperCase() + ": ";
	}

	/**
	 * Logs config at start
	 */
	@Override
	public void onInitialize() {
		FLConfig.init();
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			LOGGER.info(logKey(TRY_LIMIT_KEY) + getChunkTryLimit());
			LOGGER.info(logKey(FORCE_CLOSE_KEY) + isForceCloseEnabled().toString().toUpperCase());
			LOGGER.info(logKey(RENDER_RADIUS_KEY) + getRenderChunkRadius());
			LOGGER.info(logKey(RENDER_AREA_KEY) + getPreRenderArea());
		}
		LOGGER.info(logKey(DEBUG_KEY) + isDebugEnabled().toString().toUpperCase());
		LOGGER.info(logKey(PREGEN_RADIUS_KEY) + getPregenChunkRadius(true));
		LOGGER.info(logKey(PREGEN_AREA_KEY) + getPregenArea());
	}
}
