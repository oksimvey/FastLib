package com.robson.fastlib.api.utils;

import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;

public interface CamUtils {

static CameraParameter createParameter(FastVec3f pos, FastVec2f rot){
    return new CameraParameter(pos, rot);
}


    class CameraParameter {

        public static float rx = 0;
        public static float ry = 0;

        FastVec3f position;

       FastVec2f rotation;

        public CameraParameter(FastVec3f pos, FastVec2f rot) {
            this.position = pos;
           this.rotation = rot;
        }

        public FastVec3f getPosition() {
            return position;
        }

       public FastVec2f getRotation() {
            return rotation;
       }
    }
}
