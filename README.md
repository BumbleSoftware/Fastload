# What the hell is 441 chunk loading and why is it important?
441 Chunk loading is essentially a generator for your world that loads 21^2 chunks (or 10 chunk-render-distance), Why does 10 turn into 21? 2 sides plus the one you are standing on, squared for 2 dimensions.

# Short explanation:
Fastload is a simple mod that reduces world loading time. This serves as an alternative to ksyxis and Forcecloseloadingscreen.

# Required by:
- Client
- Server
- ...& Will work on one but not the other but some features will be disabled. So use both if you want all the features!

# Credits for mods that are integrated:
- Ksyxis by VidTu (Inompatible)
- Forcecloseloadingscreen by kennytv (Incompatible)

# Here are its features:
- Reducing 21^2 chunks to 5^2
- Cancels 'Loading Terrain' for even quicker loading times.
- Adjusts the visual chunk loading to be smaller and in sync with the smaller radius.

# Speed times: [All other elements are controlled!]
- Vanilla: [
- Create New world: ~32 sec
- Load World: ~5 sec
- ]
- Fastload: [
- Create New World: ~23 sec
- Load World: ~3 sec
- ]

# How it works (In detail) (In comparison with integrated mods):
Ksyxis cancels 441 world loading by essentially telling the game "Hey, we're done!", which works on paper. But, it is a very unsafe method of loading and rather inefficient. It does this during chunk loading which is a big no-no in code. Not only does this cause potential crashes, and corruption, but it also makes it take longer to load in comparison to our way. Ksyxis doesn't even initialise at the beginning of 441, it does it after it is initialised! Which is just plain bad. But don't get angry at ksyxis yet! The developer of it issues a firm warning that it isn't stable and really shouldn't be used, which explains everything.

We do this in a better, safer way by simply dialing down the number of chunks required to load before 441 initialises. This is fairly self-explanatory! To make world loading faster, we integrated yet another 'unstable' mod, Forcecloseloadingscreen. Like ksyxis, it also issues a firm warning of instability. How did we improve this? Just by initialising the pre-renderer earlier in the world initiation phase as well as preventing the 'Pause Menu' activating when not focused on the window until the real renderer initialised. We also make it cancel only when the player spawns, to avoid issues where the player spawns in the wrong locations with incorrect playerdata. Thus, fixing the bugs that mods brings in too! There's your technical explanation!

Modrinth: https://modrinth.com/mod/fastload

Curseforge: https://www.curseforge.com/minecraft/mc-mods/fastload

Discord: https://discord.gg/fMSnenNSXM
