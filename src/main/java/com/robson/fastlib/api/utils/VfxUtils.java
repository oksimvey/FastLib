package com.robson.fastlib.api.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.particle.EpicFightParticles;

public interface VfxUtils {

    static void spawnAfterImageParticle(LivingEntity entity){
        if (entity != null){
            Minecraft.getInstance().particleEngine.createParticle(EpicFightParticles.WHITE_AFTERIMAGE.get(),
                    entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0 ,0);

        }
    }

    static void spawnBlockBreakParticle(LivingEntity entity, float radius){
        if (entity == null || entity.level().isClientSide) return;
        LevelUtil.circleSlamFracture(entity, entity.level(), entity.position().subtract(0, 0.1,0), radius);
    }



}
