# Short explanation
Fastload is a *relatively* simple mod that changes some crucial parts of world loading to speed it up.

# Environments
- Client (Optional)
- Server (Optional)

# Credits
- StockiesLad (Owner)
- JoostMSoftware (Co-Owner)
- AbdElAziz333 (Forge)
- VidTu (Ksyxis (Idea))
- kennytv (Forcecloseloadingscreen (Code))

# Features
- 441 Pre-generator: Entirely yanks it to sync chunk generation with chunk rendering.
- Loading Screens: Cancellable (Configurable)
- Rendering: Pre-rendering Phase (Configurable)

# Suggested With
- C2ME: Optimises chunk generation.
- Sodium: Optimises chunk rendering.

# Explanation
The 441 chunk loading engine is pretty much what the name is. The reason for it being 441 chunks is that
it generates chunks in a 10 chunk radius as a square. So multiply that by 2, add 1 (because you are standing
on a chunk) and square it. Fastload removes this *feature* as it is a huge waste of time. Instead, it will
only load 1 chunk (the spawn chunk) and loads the rest of the spawn region during early rendering.

Along with that, it also replaces some of the loading screens with its own implementation. This pretty much
allows rendering to begin immediately instead of it being halted until the player spawn packet is received.

# Links
- Modrinth: https://modrinth.com/mod/fastload
- Curseforge: https://www.curseforge.com/minecraft/mc-mods/fastload
- Discord: https://discord.gg/fMSnenNSXM
