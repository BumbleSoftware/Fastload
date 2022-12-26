package com.abdelaziz.fastload.init;

import com.abdelaziz.fastload.client.FLClientEvents;
import com.abdelaziz.fastload.client.FLClientHandler;
import com.abdelaziz.fastload.config.init.FLConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.abdelaziz.fastload.config.init.DefaultConfig.propertyKeys.*;
import static com.abdelaziz.fastload.config.init.FLMath.*;

public class Fastload {
    public static final String NAMESPACE = "Fastload";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    public Fastload() {
        MinecraftForge.EVENT_BUS.addListener(this::clientSetup);

        FLConfig.init();
        if (FMLLoader.getDist() == Dist.CLIENT) {
            LOGGER.info(loggableString(tryLimit()) + getChunkTryLimit());
            LOGGER.info(loggableString(unsafeClose()) + getCloseUnsafe().toString().toUpperCase());
            LOGGER.info(loggableString(render(true), "radius") + "" + getPreRenderRadius());
            LOGGER.info(loggableString(render(true), "area") + getPreRenderArea());
        }

        LOGGER.info(loggableString(debug()) + getDebug().toString().toUpperCase());
        LOGGER.info(loggableString(pregen(true), "radius") + getPregenRadius(true));
        LOGGER.info(loggableString(pregen(true), "area") + getPregenArea());
    }

    private static String loggableString(String key) {
        return key.toUpperCase() + ": ";
    }

    private static String loggableString(String key, String extra) {
        return key.toUpperCase() + "_" + extra.toUpperCase() + ": ";
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        FLClientEvents.init();
        FLClientHandler.init();
    }
}