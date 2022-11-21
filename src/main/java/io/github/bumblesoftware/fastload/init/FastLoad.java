package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.config.FLConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.bumblesoftware.fastload.config.FLMath.*;

public class FastLoad implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Fastload");
	@Override
	public void onInitialize() {
		FLConfig.loadClass();
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			LOGGER.info("CHUNK_TRY_LIMIT: " + getChunkTryLimit());
			LOGGER.info("CANCEL_LOADING_SCREEN: " + getCloseUnsafe().toString().toUpperCase());
			LOGGER.info("PRE_RENDER_RADIUS: " + getPreRenderRadius());
			LOGGER.info("PRE_RENDER_AREA: " + getPreRenderArea());
		}
		LOGGER.info("DEBUG MODE: " + getDebug().toString().toUpperCase());
		LOGGER.info("SPAWN_CHUNK_RADIUS: " + getPregenRadius(false));
		LOGGER.info("SPAWN CHUNK AREA: " + getPregenArea());
	}
}
