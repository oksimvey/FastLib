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
        public void setTarget(Float target) {
            this.target = target;
        }

        @Override
        public void update(float deltaTime) {
            float alpha = 1f - (float)Math.exp(-factor * deltaTime);
            current += (target - current) * alpha;
        }

        @Override
        public Float getCurrent() {
            return current;
        }

}
