package com.robson.fastlib.api.keybinding;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;

public abstract class KeyBinding {

    private final KeyMapping keyMapping;

    private final BasicKey key;

    public KeyBinding(KeyMapping keyMapping, BasicKey key) {
        this.keyMapping = keyMapping;
        this.key = key;
    }

    public abstract boolean shouldHandle(Player player);

    public BasicKey getKey() {
        return key;
    }

    public KeyMapping getKeyMapping() {
        return keyMapping;
    }
}
