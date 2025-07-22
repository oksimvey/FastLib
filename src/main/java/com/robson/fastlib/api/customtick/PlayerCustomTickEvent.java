package com.robson.fastlib.api.customtick;

import net.minecraft.world.entity.player.Player;

public abstract class PlayerCustomTickEvent {

    private final byte flag;

    public PlayerCustomTickEvent(byte flag) {
        this.flag = flag;
    }

    public byte getFlag() {
        return flag;
    }

    public abstract boolean canTick(Player player);

    public abstract void onTick(Player player);
}
