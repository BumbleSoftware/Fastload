package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.common.FLCommonEvents;
import io.github.bumblesoftware.fastload.common.FLCommonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fastload {
    public static final String NAMESPACE = "Fastload";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);


    public Fastload() {
        FLCommonHandler.init();
        FLCommonEvents.init();
    }
}
