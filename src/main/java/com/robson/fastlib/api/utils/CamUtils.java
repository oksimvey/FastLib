package com.robson.fastlib.api.utils;

public interface CamUtils {

    class CameraParameter {

        float x;

        float y;

        float z;

        float yaw;

        float pitch;

        public CameraParameter(float x, float y, float z, float yaw, float pitch) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
        public float getZ() {
            return z;
        }

        public float getYaw() {
            return yaw;
        }
        public float getPitch() {
            return pitch;
        }
    }
}
