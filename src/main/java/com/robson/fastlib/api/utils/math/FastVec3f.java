package com.robson.fastlib.api.utils.math;

import net.minecraft.world.phys.Vec3;

public record FastVec3f(float x, float y, float z) {

    public static final FastVec3f ZERO = new FastVec3f(0, 0, 0);


   public static FastVec3f fromVec3(Vec3 vec3){
       return new FastVec3f((float) vec3.x, (float) vec3.y, (float) vec3.z);
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

   public float dot(FastVec3f vec3f){
       return (x * vec3f.x) + (y * vec3f.y) + (z * vec3f.z);
   }

   public FastVec3f cross(FastVec3f vec3f){
       return new FastVec3f(y * vec3f.z - z * vec3f.y, z * vec3f.x - x * vec3f.z, x * vec3f.y - y * vec3f.x);
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
