package com.robson.fastlib.api.camera;

public class SmoothFloat implements CameraSmoother<Float> {
        private float current;
        private float target;
        private final float factor;

        public SmoothFloat(float initial, float factor) {
            this.current = initial;
            this.target = initial;
            this.factor = factor;
        }

    @Override
    public void setCurrent(Float current) {
        this.current = current;
        this.target = current;
    }

    @Override
        public void setTarget(Float target) {
            this.target = target;
        }

    @Override
    public void update(float deltaTime) {
        float alpha = 1f - (float)Math.exp(-factor * deltaTime);

        // diferença angular mínima em [-180, 180]
        float diff = (target - current) % 360f;
        if (diff > 180f) diff -= 360f;
        if (diff < -180f) diff += 360f;

        current += diff * alpha;

        // manter current normalizado
        if (current >= 180f) current -= 360f;
        if (current < -180f) current += 360f;
    }


    @Override
        public Float getCurrent() {
            return current;
        }

}
