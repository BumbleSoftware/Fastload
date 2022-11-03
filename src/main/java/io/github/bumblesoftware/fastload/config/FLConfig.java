package io.github.bumblesoftware.fastload.config;

import io.github.bumblesoftware.fastload.FastLoad;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

@SuppressWarnings("SameParameterValue")
public class FLConfig {
    public static void loadClass() {}
    protected static final int RAW_CHUNK_PREGEN_RADIUS;
    protected static final int RAW_PRE_RENDER_RADIUS;
    protected static final boolean CLOSE_LOADING_SCREEN_UNSAFELY;

    static {
        final Properties properties = new Properties();
        final Properties newProperties = new Properties();
        final Path path = FabricLoader.getInstance().getConfigDir().resolve("fastload.properties");

        if (Files.isRegularFile(path)) {
            try (InputStream in = Files.newInputStream(path, StandardOpenOption.CREATE)) {
                properties.load(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        RAW_PRE_RENDER_RADIUS = getInt(properties, newProperties, "pre_render_radius", 0);
        RAW_CHUNK_PREGEN_RADIUS = getInt(properties, newProperties, "chunk_pregen_radius", 5);
        CLOSE_LOADING_SCREEN_UNSAFELY = getBoolean(properties, newProperties, "close_loading_screen_unsafely", false);

        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            newProperties.store(out, "Fastload Configuration file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void logError(String key) {
        FastLoad.LOGGER.error("Failed to parse variable '" + key + "' in Fastload's config, generating a new one!");
    }
    private static int getInt(Properties properties, Properties newProperties, String key, int def) {
        try {
            final int i = Integer.parseInt(properties.getProperty(key));
            newProperties.setProperty(key, String.valueOf(i));
            return i;
        } catch (NumberFormatException e) {
            logError(key);
            newProperties.setProperty(key, String.valueOf(def));
            return def;
        }
    }
    private static boolean getBoolean(Properties properties, Properties newProperties, String key, boolean def) {
        try {
            final boolean b = Boolean.parseBoolean(properties.getProperty(key));
            newProperties.setProperty(key, String.valueOf(b));
            return b;
        } catch (NumberFormatException e) {
            logError(key);
            newProperties.setProperty(key, String.valueOf(def));
            return def;
        }
    }
}
