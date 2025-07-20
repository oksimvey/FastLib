package com.robson.fastlib.api.keybinding;

import net.minecraft.world.entity.player.Player;

public abstract class BasicKey {

    protected boolean isPressed;

    public BasicKey() {
        this.isPressed = false;
    }

    public void onPressTick(Player player) {
        if (!this.isPressed) {
            this.onPress(player);
            this.isPressed = true;
        }
    }

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
