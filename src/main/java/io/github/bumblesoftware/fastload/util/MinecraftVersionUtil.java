package io.github.bumblesoftware.fastload.util;

import io.github.bumblesoftware.fastload.config.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import net.fabricmc.loader.api.FabricLoader;

public class MinecraftVersionUtil {
    public static boolean matchesAny(String... versions) {
        for (String version : versions)
            if (getVersion().equals(version))
                return true;
        return false;
    }

    public static boolean matchesWhen(
            String operator,
            String version
    ) {
        var currentVersionNum = Integer.parseInt(getVersion().replaceAll("\\.", ""));
        var versionNum = Integer.parseInt(version.replaceAll("\\.", ""));

        if (FLMath.isDebugEnabled())
            Fastload.LOGGER.info(currentVersionNum + operator + versionNum);

        if (operator != null)
            switch (operator) {
                case ">" -> {
                    if (currentVersionNum > versionNum)
                        return true;
                }
                case "<" -> {
                    if (currentVersionNum < versionNum)
                        return true;
                }

                case "<=", "=<" -> {
                    if (currentVersionNum <= versionNum)
                        return true;
                }

                case ">=", "=>" -> {
                    if (currentVersionNum >= versionNum)
                        return true;
                }
                default -> throw new NumberFormatException("Unsupported operator: " + operator);
            }
        return false;
    }

    @SuppressWarnings("unused")
    public static boolean matchesWhen(
            String[] operators,
            String[] versions
    ) {
        if (operators.length != versions.length)
            throw new NumberFormatException("Quantity of operators and versions are unequal. Cannot compare");
        else for (int i = 0; i < operators.length; i++) {
            if (!matchesWhen(operators[i], versions[i]))
                return false;
        }
        return true;
    }

    public static String getVersion() {
        return FabricLoader.getInstance().getModContainer("minecraft").orElseThrow().getMetadata().getVersion().getFriendlyString();
    }
}
