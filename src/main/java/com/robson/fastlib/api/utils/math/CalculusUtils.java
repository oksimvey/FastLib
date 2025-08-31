package com.robson.fastlib.api.utils.math;

import net.minecraft.world.phys.Vec3;

public interface CalculusUtils {

    static FastVec3f integrate(FastVec3f relativeVector, float speed, float interval, int size){

        FastVec3f position = FastVec3f.ZERO;
        for (float i = 0; i < size; i+= interval){
            position = position.add(relativeVector.scale(speed * interval));
        }
        return position;

    }

    static FastVec3f integrate(FastVec3f gradient, float interval, int size){
        FastVec3f position = FastVec3f.ZERO;
        for (float i = 0; i < size; i+= interval){
            position = position.add(gradient.scale(interval));
        }
        return position;
    }

    static float partialDerivative(float n1, float n2, float interval){
        return (n2 - n1)/interval;
    }

    static FastVec3f getGradient(Vec3 vec31, Vec3 vec32, float interval){
        return getGradient((float) vec31.x, (float) vec31.y, (float) vec31.z, (float) vec32.x, (float) vec32.y, (float) vec32.z, interval);
    }

    static FastVec3f getGradient(FastVec3f p1, FastVec3f p2, float interval){
        return getGradient(p1.x(), p1.y(), p1.z(), p2.x(), p2.y(), p2.z(), interval);
    }

    static FastVec3f getGradient(float x1, float y1, float z1, float x2, float y2, float z2, float interval){
        return new FastVec3f(partialDerivative(x1, x2, interval), partialDerivative(y1, y2, interval), partialDerivative(z1, z2, interval));
    }
}
