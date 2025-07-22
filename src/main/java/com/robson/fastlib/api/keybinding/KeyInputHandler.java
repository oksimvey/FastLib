package com.robson.fastlib.api.keybinding;

public class KeyInputHandler {

    private boolean up;

    private boolean down;

    private boolean left;

    private boolean right;

    public KeyInputHandler() {
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

    public short getInputAngle(){
        short angle = 0;
        if (this.up){
            if (this.left) angle += 315;
            if (this.right) angle += 45;
            return angle;
        }
        if (this.down){
            angle = 180;
            if (this.left) angle += 45;
            if (this.right) angle -= 45;
            return angle;
        }
        if (this.left) angle += 270;
        if (this.right) angle += 90;
        return angle;
    }

    public short getInputAngleInverted(){
        short angle = 0;
        if (this.up){
            if (this.left) angle += 45;
            if (this.right) angle += 315;
            return angle;
        }
        if (this.down){
            if (this.left) angle -= 45;
            if (this.right) angle += 45;
            return angle;
        }
        return angle;
    }
}
