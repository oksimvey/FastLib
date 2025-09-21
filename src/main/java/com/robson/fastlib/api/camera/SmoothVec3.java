package com.robson.fastlib.api.camera;

import com.robson.fastlib.api.utils.math.FastVec3f;

public class SmoothVec3 implements CameraSmoother<FastVec3f> {

    private FastVec3f current;
    private FastVec3f target;
    private final float factor;

    public SmoothVec3(FastVec3f initial, float factor) {
        this.current = initial.copy();
        this.target = initial.copy();
        this.factor = factor;
    }

    public void setCurrent(FastVec3f current) {
        this.current = current;
    }

    @Override
    public void setTarget(FastVec3f target) {
        this.target = target.copy();
    }

    @Override
    public void update(float deltaTime) {
        float alpha = (1f - (float)Math.exp(-factor * deltaTime));

        FastVec3f diff = target.sub(current).scale(alpha);
        current = current.add(diff);
    }

    @Override
    public FastVec3f getCurrent() {
        return current.copy();
    }
}
