package io.github.bumblesoftware.fastload.version;

import static io.github.bumblesoftware.fastload.version.VersionHelper.MatchingStrategy.REGEX;
import static io.github.bumblesoftware.fastload.version.VersionHelper.ONLY_FIRST_MAJOR;
import static io.github.bumblesoftware.fastload.version.VersionHelper.matchesAny;

public class VersionConstants {
    public static void init() {}

    public static final boolean IS_MINECRAFT_1200;
    public static final boolean IS_MINECRAFT_1194;
    public static final boolean IS_MINECRAFT_1192;
    public static final boolean IS_MINECRAFT_1193;
    public static final boolean IS_MINECRAFT_1191;
    public static final boolean IS_MINECRAFT_1190;
    public static final boolean IS_MINECRAFT_1182;


    static {
        IS_MINECRAFT_1200 = matchesAny("1.20", REGEX);
        IS_MINECRAFT_1194 = matchesAny( "1.19.4");
        IS_MINECRAFT_1193 = matchesAny( "1.19.3");
        IS_MINECRAFT_1192 = matchesAny( "1.19.2");
        IS_MINECRAFT_1191 = matchesAny( "1.19.1");
        IS_MINECRAFT_1190 = matchesAny("1.19", REGEX, ONLY_FIRST_MAJOR);
        IS_MINECRAFT_1182 = matchesAny( "1.18.2");
    }
}
