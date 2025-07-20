package com.robson.fastlib.api.keybinding;

public class IInput {

    private boolean up;

    private boolean down;

    private boolean left;

    private boolean right;

    public IInput() {
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
