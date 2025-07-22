package com.robson.fastlib.api.keybinding;

import com.robson.fastlib.api.utils.math.FastLibMathUtils;

public class MouseInputHandler {

    private float dx;

    private float dy;

    private float angle;

    public MouseInputHandler() {
        this.dx = 0;
        this.dy = 0;
    }

    public void handleMouseInput(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
        float angle = FastLibMathUtils.radiansToDegree((float) Math.atan2(-(dx), -(dy)));
        if (angle <= 0){
            angle = -(angle);
        }
        else angle = 360 - angle;
        this.angle = angle;

    }

    public float getAngle() {
        return angle;
    }

    public float getDy() {
        return dy;
    }

    public float getDx() {
        return dx;
    }
}
