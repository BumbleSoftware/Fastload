package io.github.bumblesoftware.fastload.util;

import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.init.Fastload;
import net.minecraft.SharedConstants;

public class MinecraftVersionUtil {
    public static boolean compareAll(String... versions) {
        for (String version : versions)
            if (getVersion().equals(version))
                return true;
        return false;
    }

    public static boolean compareWithOperator(
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

    public static boolean compareWithOperator(
            String[] operators,
            String[] versions
    ) {
        if (operators.length != versions.length)
            throw new NumberFormatException("Quantity of operators and versions are unequal. Cannot compare");
        else for (int i = 0; i < operators.length; i++) {
            if (!compareWithOperator(operators[i], versions[i]))
                return false;
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    private static String getVersion() {
        return SharedConstants.VERSION_NAME;
    }
}
