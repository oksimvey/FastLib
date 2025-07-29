package com.robson.fastlib.api.utils.math;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;
import yesman.epicfight.api.utils.math.OpenMatrix4f;

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

    public static PoseStack correctPoseStack(PoseStack stack, OpenMatrix4f matrix4f){
        PoseStack newstack = new PoseStack();
        newstack.pushPose();
        newstack.last().pose().set(stack.last().pose());
        newstack.last().pose().mul(matrix4f.m00, matrix4f.m01, matrix4f.m02, matrix4f.m03,
                matrix4f.m10, matrix4f.m11, matrix4f.m12, matrix4f.m13,
                matrix4f.m20, matrix4f.m21, matrix4f.m22, matrix4f.m23,
                matrix4f.m30, matrix4f.m31, matrix4f.m32, matrix4f.m33);
        return newstack;
    }

}
