package com.robson.fastlib.api.utils.math;

import net.minecraft.world.phys.Vec3;

public interface FastLibMathUtils {

    float EULER = 2.718281f;

    float PI = 3.141592f;

    static float degreeToRadians(float degree) {
        return PI * (degree / 180f);
    }

    static FastVec3f rotate2DVector(FastVec3f vec, float degrees) {
        float theta = degreeToRadians(degrees);
        float x = (float) ((vec.x() * Math.cos(theta)) - (vec.z() * Math.sin(theta)));
        float z = (float) ((vec.x() * Math.sin(theta)) + (vec.z() * Math.cos(theta)));
        return new FastVec3f(x, vec.y(), z);
    }
}
