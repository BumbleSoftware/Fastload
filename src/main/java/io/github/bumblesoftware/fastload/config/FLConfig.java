package io.github.bumblesoftware.fastload.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class FLConfig {
    public static void loadClass() {}

    public static final int CHUNK_PREGEN_RADIUS;
    public static final boolean CLOSE_LOADING_SCREEN_UNSAFELY;

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
        CHUNK_PREGEN_RADIUS = getInt(properties, newProperties);
        CLOSE_LOADING_SCREEN_UNSAFELY = getBoolean(properties, newProperties);
        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            newProperties.store(out, "Fastload Config");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getInt(Properties properties, Properties newProperties) {
        try {
            final int i = Integer.parseInt(properties.getProperty("chunk_pregen_radius"));
            newProperties.setProperty("chunk_pregen_radius", String.valueOf(i));
            return i;
        } catch (NumberFormatException e) {
            newProperties.setProperty("chunk_pregen_radius", String.valueOf(5));
            return 5;
        }
    }

    private static boolean getBoolean(Properties properties, Properties newProperties) {
        try {
            final boolean b = parseBoolean(properties.getProperty("close_loading_screen_unsafely"));
            newProperties.setProperty("close_loading_screen_unsafely", String.valueOf(b));
            return b;
        } catch (NumberFormatException e) {
            newProperties.setProperty("close_loading_screen_unsafely", String.valueOf(true));
            return true;
        }
    }

    private static boolean parseBoolean(String string) {
        if (string == null) throw new NumberFormatException("null");
        if (string.trim().equalsIgnoreCase("true")) return true;
        if (string.trim().equalsIgnoreCase("false")) return false;
        throw new NumberFormatException(string);
    }
}
