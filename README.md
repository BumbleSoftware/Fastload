# Short explanation:
Fastload is a mod that provides numerous methods of control over world loading. Most of which are intended
to optimise it; to speed it up.

# Environments:
- Client (Optional)
- Server (Optional)

# Credits:
- StockiesLad (Owner)
- JoostMSoftware (Co-Owner)
- AbdElAziz333 (Forge)
- VidTu (Ksyxis (Idea))
- kennytv (Forcecloseloadingscreen (Code))

# Features:
- Chunk Pre-generation: Reducing 21^2 chunks to 5^2 (Configurable)
- Loading Screens: Cancellable (Configurable)
- Rendering: Pre-rendering Phase (Configurable)

# Suggested With:
- C2ME: Optimises chunk generation.
- Sodium: Optimises chunk rendering.

# Speed times (Default Settings, My system):
- Vanilla:
  - Create New world: ~32 sec
  - Load World: ~5 sec

- Fastload: 
  - Create New World: ~23 sec
  - Load World: ~3 sec

# Explanation:
The 441 chunk loading engine is pretty much what the name is. The reason for it being 441 chunks is that
it generates chunks in a 10 chunk radius as a square. So multiply that by 2, add 1 (because you are standing
on a chunk) and square it. Fastload simply adjusts the radius to be lower by default, whilst also making it
changeable like every other chunk radius.

During world loading, there is also a lot of pretty much unnecessary loading screens. Fastload makes them
skippable. To replace this, there is a pre-rendering phase that has a customisable radius as well. It allows
chunks to be generated uniformly around you, unlike vanilla behaviour which only renders visible chunks.
If you really wanted to, you could skip loading screens & 441 chunk loading directly to this phase. It will
be faster with 0 downsides.

# Links
- Modrinth: https://modrinth.com/mod/fastload
- Curseforge: https://www.curseforge.com/minecraft/mc-mods/fastload
- Discord: https://discord.gg/fMSnenNSXM
