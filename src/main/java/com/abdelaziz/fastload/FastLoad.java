package com.abdelaziz.fastload;

import com.abdelaziz.fastload.config.FLConfig;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.abdelaziz.fastload.config.FLMath.*;

@Mod("fastload")
public class FastLoad {
	public static final Logger LOGGER = LoggerFactory.getLogger("Fastload");

	public FastLoad() {
		FLConfig.loadClass();
		LOGGER.info("CHUNK_TRY_LIMIT: " + getChunkTryLimit());
		LOGGER.info("CANCEL_LOADING_SCREEN: " + getCloseUnsafe().toString().toUpperCase());
		LOGGER.info("DEBUG MODE: " + getDebug().toString().toUpperCase());
		LOGGER.info("SPAWN_CHUNK_RADIUS: " + getPregenRadius(false));
		LOGGER.info("SPAWN CHUNK AREA: " + getPregenArea());
		LOGGER.info("PRE_RENDER_RADIUS: " + getPreRenderRadius());
		LOGGER.info("PRE_RENDER_AREA: " + getPreRenderArea());
	}
}