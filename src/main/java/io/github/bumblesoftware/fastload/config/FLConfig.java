package io.github.bumblesoftware.fastload.config;

import io.github.bumblesoftware.fastload.FastLoad;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

@SuppressWarnings("SameParameterValue")
public class FLConfig {
    public static void loadClass() {}

    //Config Variables
    protected static final int RAW_CHUNK_TRY_LIMIT;
    protected static final boolean CLOSE_LOADING_SCREEN_UNSAFELY;
    protected static final boolean DEBUG;
    protected static final int RAW_CHUNK_PREGEN_RADIUS;
    protected static final int RAW_PRE_RENDER_RADIUS;


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

        //Don't forget that these variables are sorted alphabetically in .properties files!
        RAW_CHUNK_TRY_LIMIT = getInt(properties, newProperties, "chunk_try_limit", 100);
        CLOSE_LOADING_SCREEN_UNSAFELY = getBoolean(properties, newProperties, "close_loading_screen_unsafely", false);
        DEBUG = getBoolean(properties, newProperties, "debug", false);
        RAW_CHUNK_PREGEN_RADIUS = getInt(properties, newProperties, "pre_generator_chunk_radius", 5);
        RAW_PRE_RENDER_RADIUS = getInt(properties, newProperties, "pre_render_radius", 0);

        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            newProperties.store(out, "Fastload Configuration file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedWriter comment = Files.newBufferedWriter(path, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            comment.write("\n");
            comment.write("\n# Definitions");
            comment.write("\n# 'chunk_try_limit' = how many times in a raw should the same count of loaded chunks be ignored before we cancel pre-rendering");
            comment.write("\n# There are no limits for this. Must be a positive Integer");
            comment.write("\n#");
            comment.write("\n# 'close_loading_screen_unsafely' = should skip 'Joining World', and 'Downloading Terrain'. Potentially can result in joining world before chunks are properly loaded");
            comment.write("\n# Enabled = true, Disabled = false");
            comment.write("\n#");
            comment.write("\n# 'debug' = debug (log) all things happening in fastload to aid in diagnosing issues.");
            comment.write("\n# Enabled = true, Disabled = false");
            comment.write("\n#");
            comment.write("\n# 'pre_render_radius' = how many chunks are loaded until 'building terrain' is completed. Adjusts with FOV to decide how many chunks are visible");
            comment.write("\n# Min = 0, Max = 32 or your render distance, Whichever is smaller. Set 0 to disable. Must be a positive Integer");
            comment.write("\n#");
            comment.write("\n# 'pregen_chunk_radius' = how many chunks (from 441 Loading) are pre-generated until the server starts");
            comment.write("\n# Min = 0, Max = 32. Set 0 to only pregen 1 chunk. Must be a positive Integer");


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
