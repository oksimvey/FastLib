package com.robson.fastlib.api.utils.math;

import net.minecraft.world.phys.Vec3;

public record FastVec3f(float x, float y, float z) {

   public FastVec3f rotate(float angle){
       return FastLibMathUtils.rotate2DVector(this, angle);
   }

   public static FastVec3f fromVec3(Vec3 vec3){
       return new FastVec3f((float) vec3.x, (float) vec3.y, (float) vec3.z);
   }

   public Vec3 toVec3(){
       return new Vec3(x, y, z);
   }
}
