package io.github.bumblesoftware.fastload.config.init;

import io.github.bumblesoftware.fastload.init.Fastload;
import io.github.bumblesoftware.fastload.util.MinMaxHolder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;

public class FLConfig {
    public static void init() {}

    //Init Vars
    private static final Properties properties;
    private static final Path path;

    //Config Variables
    protected static boolean getRawDebug() {
        return getBoolean(DEBUG_KEY, DEF_DEBUG_VALUE);
    }
    protected static boolean getRawServerRender() {
        return getBoolean(SERVER_RENDER_KEY, DEF_SERVER_RENDER);
    }
    protected static int getRawChunkTryLimit() {
        return getInt(CHUNK_TRY_LIMIT_KEY, DEF_TRY_LIMIT_VALUE, CHUNK_TRY_LIMIT_BOUND);
    }
    protected static int getRawRenderRadius() {
        return getInt(RENDER_RADIUS_KEY, DEF_RENDER_RADIUS_VALUE, CHUNK_RADIUS_BOUND);
    }

    static {
        properties = new Properties();
        path = FabricLoader.getInstance().getConfigDir().resolve(Fastload.NAMESPACE.toLowerCase() + ".properties");

        if (Files.isRegularFile(path)) {
            try (InputStream in = Files.newInputStream(path, StandardOpenOption.CREATE)) {
                properties.load(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Don't forget that these variables are sorted alphabetically in .properties files!
        getRawChunkTryLimit();
        getRawDebug();
        getRawRenderRadius();
        getRawServerRender();

        writeToDisk();

    }
    private static void logWarn(String key) {
        Fastload.LOGGER.warn("Failed to parse variable '" + key + "' in " + Fastload.NAMESPACE + "'s config, " +
                "generating a new one!");
    }

    public static void writeToDisk() {
        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            properties.store(out,  Fastload.NAMESPACE +  " Configuration File");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedWriter comment = Files.newBufferedWriter(path, StandardOpenOption.APPEND, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            comment.write("\n# Definitions");
            comment.write("\n# " + writable(DEBUG_KEY) + " = debug (logWarn) all things happening in fastload to aid in " +
                    "diagnosing issues.");
            comment.write("\n# Enabled = true, Disabled = false");
            comment.write("\n#");
            comment.write("\n# " + writable(RENDER_RADIUS_KEY) + " = how many chunks are loaded until 'building terrain' is " +
                    "completed.");
            comment.write("\n# Min = 0, Max = 32 or your render distance, Whichever is smaller. Set 0 to disable.");
            comment.write("\n#");
            comment.write("\n# " + writable(CHUNK_TRY_LIMIT_KEY) + " = how many times in a row should the same count of loaded chunks " +
                    "be ignored before we cancel pre-rendering.");
            comment.write("\n# Min = 1, Max = 1000. Set 1000 for infinity");
            comment.write("\n#");
            comment.write("\n# " + writable(SERVER_RENDER_KEY) + " = should fastload's rendering apply for servers as " +
                    "well?");
            comment.write("\n# Enabled = true, Disabled = false");
            comment.write("\n#");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String writable(String key) {
        return "'" + key.toLowerCase() + "'";
    }
    private static int getInt(String key, int def, MinMaxHolder holder) {
        try {
            int i = parseMinMax(Integer.parseInt(properties.getProperty(key)), holder);
            properties.setProperty(key, String.valueOf(i));
            return i;
        } catch (NumberFormatException e) {
            logWarn(key);
            properties.setProperty(key, String.valueOf(def));
            return def;
        }
    }

    private static boolean parseBoolean(String string) {
        if (string == null) throw new NumberFormatException("null");
        if (string.trim().equalsIgnoreCase("true")) return true;
        if (string.trim().equalsIgnoreCase("false")) return false;
        throw new NumberFormatException(string);
    }
    @SuppressWarnings("SameParameterValue")
    private static boolean getBoolean(String key, boolean def) {
        try {
            final boolean b = parseBoolean(properties.getProperty(key));
            properties.setProperty(key, String.valueOf(b));
            return b;
        } catch (NumberFormatException e) {
            logWarn(key);
            properties.setProperty(key, String.valueOf(def));
            return def;
        }
    }
    public static void storeProperty(String key, String value) {
        if (isDebugEnabled()) Fastload.LOGGER.info(key + ":" + value);
        properties.setProperty(key, value);
    }
}
