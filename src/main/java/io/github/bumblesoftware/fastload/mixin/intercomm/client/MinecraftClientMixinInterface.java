package io.github.bumblesoftware.fastload.mixin.intercomm.client;

/**
 * Used by MinecraftClientMixin to communicate with the players' network handlers
 */
public interface MinecraftClientMixinInterface {
     void canPlayerLoad();
     void gameJoined();
}
