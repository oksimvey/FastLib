package com.robson.fastlib.api.customtick;

public abstract class PlayerCustomTickEvent {

    private final byte flag;

    public PlayerCustomTickEvent(byte flag) {
        this.flag = flag;
    }

    public abstract void onTick();
}
