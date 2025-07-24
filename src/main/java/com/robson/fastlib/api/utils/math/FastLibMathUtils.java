package com.robson.fastlib.api.utils.math;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FastLibMathUtils {

    public static final float EULER = 2.718281f;

    public static final float PI = 3.141592f;

    private static final float DEGREE_TO_RADIANS = PI / 180f;

    private static final float RADIANS_TO_DEGREE = 180f / PI;

    public static float degreeToRadians(float degree) {
        return DEGREE_TO_RADIANS * degree;
    }

    public static float radiansToDegree(float radians) {
        return RADIANS_TO_DEGREE * radians;
    }

    public static FastVec3f rotate3DVector(FastVec3f vec, float degrees) {
        float theta = degreeToRadians(degrees);
        float x = (float) ((vec.x() * Math.cos(theta)) - (vec.z() * Math.sin(theta)));
        float z = (float) ((vec.x() * Math.sin(theta)) + (vec.z() * Math.cos(theta)));
        return new FastVec3f(x, vec.y(), z);
    }

    public static FastVec2f rotate2DVector(FastVec2f vec, float degrees) {
        float theta = degreeToRadians(degrees);
        float x = (float) ((vec.x() * Math.cos(theta)) - (vec.y() * Math.sin(theta)));
        float y = (float) ((vec.x() * Math.sin(theta)) + (vec.y() * Math.cos(theta)));
        return new FastVec2f(x, y);
    }

    public static float correctDegrees(float degrees) {
        if (degrees > 360) degrees %= 360f;
        return degrees;
    }

    public static AABB createAABBAroundPos(FastVec3f pos, float size) {
        return new AABB(pos.x() + size, pos.y() + size * 1.5, pos.z() + size, pos.x() - size, pos.y() - size, pos.z() - size);
    }


    public static AABB createAABBAroundEnt(Entity ent, float size) {
        return new AABB(ent.getX() + size, ent.getY() + size * 1.5, ent.getZ() + size, ent.getX() - size, ent.getY() - size, ent.getZ() - size);
    }

}
