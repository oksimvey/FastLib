package com.robson.fastlib.api.keybinding;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;

public abstract class BasicKey {

    protected boolean isPressed;

    private final KeyMapping mapping;

    public BasicKey(KeyMapping mapping) {
        this.mapping = mapping;
        this.isPressed = false;
    }

    public void onPressTick(Player player) {
        if (!this.isPressed) {
            this.onPress(player);
            this.isPressed = true;
        }
    }

    public KeyMapping getKeyMapping(){
        return mapping;
    }


    public abstract boolean shouldHandle(Player player);

    public boolean isPressed() {
        return this.isPressed;
    }

    public void onRelease(Player player) {
        this.onReleaseAction(player);
        this.isPressed = false;
    }

    protected void onReleaseAction(Player player) {
    }

    public abstract void onPress(Player player);
}
