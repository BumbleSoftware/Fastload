package io.github.bumblesoftware.fastload.config.init;

import io.github.bumblesoftware.fastload.util.MinMaxHolder;
import io.github.bumblesoftware.fastload.init.FastLoad;
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
import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.propertyKeys.*;

public class FLConfig {
    public static void loadClass() {}

    //Init Vars
    private static final Properties properties;
    private static final Path path;

    //Config Variables
    protected static int getChunkTryLimit() {
        return getInt(tryLimit(), getTryLimit(), getTryLimitBound());
    }
    protected static int getRawChunkPregenRadius() {
        return getInt(pregen(), getPregenRadius(), getRawRadiusBound());
    }
    protected static int getRawPreRenderRadius() {
        return getInt(render(), getRenderRadius(), getRawRadiusBound());
    }
    protected static boolean getCloseLoadingScreenUnsafely() {
        return getBoolean(unsafeClose(), getCloseUnsafely());
    }
    protected static boolean getRawDebug() {
        return getBoolean(debug(), getDebug());
    }

    static {
        properties = new Properties();
        path = FabricLoader.getInstance().getConfigDir().resolve(FastLoad.NAMESPACE.toLowerCase() + ".properties");

        if (Files.isRegularFile(path)) {
            try (InputStream in = Files.newInputStream(path, StandardOpenOption.CREATE)) {
                properties.load(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Don't forget that these variables are sorted alphabetically in .properties files!
        getChunkTryLimit();
        getRawChunkPregenRadius();
        getRawPreRenderRadius();
        getCloseLoadingScreenUnsafely();
        getRawDebug();

        write();

    }
    private static void logError(String key) {
        FastLoad.LOGGER.error("Failed to parse variable '" + key + "' in " + FastLoad.NAMESPACE + "'s config, generating a new one!");
    }

    private static void write() {
        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            properties.store(out,  FastLoad.NAMESPACE +  " Configuration File");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedWriter comment = Files.newBufferedWriter(path, StandardOpenOption.APPEND, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            comment.write("\n# Definitions");
            comment.write("\n# " + writable(tryLimit()) + " = how many times in a row should the same count of loaded chunks be ignored before we cancel pre-rendering.");
            comment.write("\n# Min = 1, Max = 1000. Set 1000 for infinity");
            comment.write("\n#");
            comment.write("\n# " + writable(unsafeClose()) + " = should skip 'Joining World', and 'Downloading Terrain'. Potentially can result in joining world before chunks are properly loaded");
            comment.write("\n# Enabled = true, Disabled = false");
            comment.write("\n#");
            comment.write("\n# " + writable(debug()) + " = debug (log) all things happening in fastload to aid in diagnosing issues.");
            comment.write("\n# Enabled = true, Disabled = false");
            comment.write("\n#");
            comment.write("\n# " + writable(render()) + " = how many chunks are loaded until 'building terrain' is completed. Adjusts with FOV to decide how many chunks are visible");
            comment.write("\n# Min = 0, Max = 32 or your render distance, Whichever is smaller. Set 0 to disable.");
            comment.write("\n#");
            comment.write("\n# " + writable(pregen()) + " = how many chunks (from 441 Loading) are pre-generated until the server starts");
            comment.write("\n# Min = 0, Max = 32. Set 0 to only pregen 1 chunk.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String writable(String key) {
        return "'" + key.toLowerCase() + "'";
    }
    private static int getInt(String key, int def, MinMaxHolder holder) {
        try {
            int i = FLMath.parseMinMax(Integer.parseInt(properties.getProperty(key)), holder);
            properties.setProperty(key, String.valueOf(i));
            return i;
        } catch (NumberFormatException e) {
            logError(key);
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
    private static boolean getBoolean(String key, boolean def) {
        try {
            final boolean b = parseBoolean(properties.getProperty(key));
            properties.setProperty(key, String.valueOf(b));
            return b;
        } catch (NumberFormatException e) {
            logError(key);
            properties.setProperty(key, String.valueOf(def));
            return def;
        }
    }
    public static void storeProperty(String key, String value) {
        properties.setProperty(key, value);
        System.out.println(key + ":" + value);
    }
    public static void writeToDisk() {
        write();
    }
}
