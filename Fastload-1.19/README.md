# Fastload

The '441 Loading Engine' is simply a pregenerator for a 10 chunk render distance. Why not 10 Squared? No, remember that there are two sides. 20? Still no, because you have to account for the chunk you spawn in. Which makes it 21 Squared.

This system is not efficient though, and consumes a lot of time. Ksyxis, which was used to mitigate this uses an unsafe way of cancelling it. Pregeneration is still important however, so we made it 2 render distance. Enough to account for local spawn.

We also added a tiny mixin which permits render ticking so that your game will be better prepared for an early entry into your world.

Modrinth: https://modrinth.com/mod/fastload

Curseforge:
https://www.curseforge.com/minecraft/mc-mods/fastload
