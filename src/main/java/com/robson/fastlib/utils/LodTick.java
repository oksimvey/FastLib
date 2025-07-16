package com.robson.fastlib.utils;

import com.robson.fastlib.api.data.structures.FastDataParameter;
import com.robson.fastlib.api.data.structures.FastTemporaryList;
import com.robson.fastlib.api.data.structures.FastTemporaryMap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class LodTick {

    private static final FastTemporaryList<Entity> FLAGGED_ENTITIES = new FastTemporaryList<>(FastDataParameter.DataType.ENTITY);

    private static final FastTemporaryMap<Entity, Integer> FLAGGED_NEXT_TICK = new FastTemporaryMap<>(FastDataParameter.DataType.ENTITY);

    private static final FastTemporaryList<Vec3> FLAGGED_VECTORS = new FastTemporaryList<>(FastDataParameter.DataType.VECTORS);

    private static final FastTemporaryMap<Vec3, Integer> FLAGGED_VECTORS_NEXT_TICK = new FastTemporaryMap<>(FastDataParameter.DataType.VECTORS);

    public static boolean canTick(Entity entity, float multiplier){
        if (entity == null || FLAGGED_ENTITIES.contains(entity) || Minecraft.getInstance().player == null) return false;
        short distance = (short) (computeDistance(entity.position()) * multiplier);
        int tickNow = Minecraft.getInstance().player.tickCount;
        if (FLAGGED_NEXT_TICK.contains(entity)) {
            int nextTick = FLAGGED_NEXT_TICK.get(entity);
            if (tickNow < nextTick) return false;
        }

        int remainder = tickNow % distance;
        if (remainder != 0) {
            int nextTick = tickNow + (distance - remainder);
            FLAGGED_NEXT_TICK.put(entity, nextTick);
            return false;
        }
        FLAGGED_NEXT_TICK.remove(entity);
        return true;

    }

    public static boolean canTick(Vec3 vec3, float multiplier){
        if (vec3 == null || FLAGGED_VECTORS.contains(vec3) || Minecraft.getInstance().player == null) return false;
        short distance = (short) (computeDistance(vec3) * multiplier);
        int tickNow = Minecraft.getInstance().player.tickCount;
        if (FLAGGED_VECTORS_NEXT_TICK.contains(vec3)) {
            int nextTick = FLAGGED_VECTORS_NEXT_TICK.get(vec3);
            if (tickNow < nextTick) return false;
        }
        int remainder = tickNow % distance;
        if (remainder != 0) {
            int nextTick = tickNow + (distance - remainder);
            FLAGGED_VECTORS_NEXT_TICK.put(vec3, nextTick);
            return false;
        }
        FLAGGED_VECTORS_NEXT_TICK.remove(vec3);
        return true;
    }

    static short computeDistance(Vec3 vec3){
        if (vec3 == null || Minecraft.getInstance().gameRenderer.getMainCamera() == null){
            return 1;
        }
        return (short) (1 + (Math.pow(1.075, Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().distanceTo(vec3))));
    }

}
