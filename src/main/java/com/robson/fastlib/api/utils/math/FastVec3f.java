package com.robson.fastlib.api.utils.math;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.utils.math.OpenMatrix4f;

public record FastVec3f(float x, float y, float z) {

    public static final FastVec3f ZERO = new FastVec3f(0, 0, 0);

    public static FastVec3f fromTranslatedMatrix(OpenMatrix4f matrix4f, float x, float y, float z) {
        float x1 = matrix4f.m30 + matrix4f.m00 * x + matrix4f.m10 * y + matrix4f.m20 * z;
        float y1 = matrix4f.m31 + matrix4f.m01 * x + matrix4f.m11 * y + matrix4f.m21 * z;
        float z1 = matrix4f.m32 + matrix4f.m02 * x + matrix4f.m12 * y + matrix4f.m22 * z;
        return new FastVec3f(x1, y1, z1);

    }

    public static FastVec3f fromMatrix4f(OpenMatrix4f matrix4f) {
        return new FastVec3f(matrix4f.m30, matrix4f.m31, matrix4f.m32);
    }

    public FastVec3f toGlobalPos(LivingEntity entity){
        return this.rotate(entity.yBodyRotO).add((float) entity.getX(), (float) entity.getY(), (float) entity.getZ());
    }



    public static FastVec3f fromVec3(Vec3 vec3){
       return new FastVec3f((float) vec3.x, (float) vec3.y, (float) vec3.z);
   }

   public FastVec3f copy(){
       return new FastVec3f(x, y, z);
   }

   public Vec3 toVec3(){
       return new Vec3(x, y, z);
   }

   public FastVec3f add(FastVec3f vec3f){
       return new FastVec3f(x + vec3f.x, y + vec3f.y, z + vec3f.z);
   }

   public FastVec3f add(float x, float y, float z){
       return new FastVec3f(this.x + x, this.y + y, this.z + z);
   }

   public FastVec3f sub(FastVec3f vec3f){
       return new FastVec3f(x - vec3f.x, y - vec3f.y, z - vec3f.z);
   }

   public FastVec3f sub(float x, float y, float z){
       return new FastVec3f(this.x - x, this.y - y, this.z - z);
   }

   public FastVec3f scale(float scale){
       return new FastVec3f(x * scale, y * scale, z * scale);
   }

   public FastVec3f div(float scale){
       if (scale == 0) return new FastVec3f(0 ,0 ,0);
       return new FastVec3f(x / scale, y / scale, z / scale);
   }

   public float distanceTo(FastVec3f vec3f){
       return vec3f.sub(this).length();
   }

   public float length(){
       return (float) Math.sqrt((x * x) + (y * y) + (z * z));
   }

   public FastVec3f normalize(){
       return div(length());
   }

    public FastVec3f rotate(float angle){
        return FastLibMathUtils.rotate3DVector(this, angle);
    }
}
