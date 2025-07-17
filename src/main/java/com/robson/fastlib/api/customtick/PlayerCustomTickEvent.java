package com.robson.fastlib.api.customtick;

import net.minecraft.world.entity.player.Player;

public abstract class PlayerCustomTickEvent {

    private final byte flag;

    public PlayerCustomTickEvent(byte flag) {
        this.flag = flag;
    }

    public abstract void onTick(Player player);
}
