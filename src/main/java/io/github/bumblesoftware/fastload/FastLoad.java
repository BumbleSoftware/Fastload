package io.github.bumblesoftware.fastload;

import io.github.bumblesoftware.fastload.config.FLConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.bumblesoftware.fastload.config.FLMath.*;

public class FastLoad implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Fastload");
	@Override
	public void onInitialize() {
		FLConfig.loadClass();
		LOGGER.info("HARD_CODED_RENDER_RADIUS_CAP: " + getRenderRadiusBound());
		LOGGER.info("SPAWN_CHUNK_RADIUS: " + getPregenRadius(true));
		LOGGER.info("SPAWN CHUNK AREA: " + getPregenArea());
		LOGGER.info("PRE_RENDER_RADIUS: " + getPreRenderRadius());
		LOGGER.info("PRE_RENDER_AREA: " + getPreRenderArea());
		LOGGER.info("CANCEL_LOADING_SCREEN: " + getCloseUnsafe().toString().toUpperCase());
	}
}
