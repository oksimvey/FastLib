package com.robson.fastlib.api.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public interface TargetUtils {

    static void setTarget(LivingEntity entity, LivingEntity target){
        if (entity != null && target != null){
            LivingEntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
            if (entityPatch != null){
                entityPatch.setGrapplingTarget(target);
                if (entityPatch instanceof MobPatch){
                    ((MobPatch<?>)entityPatch).setAttakTargetSync(target);
                }
            }
            else if (entity instanceof Mob){
                ((Mob)(entity)).setTarget(target);
            }
        }
    }

    static LivingEntity getTarget(LivingEntity entity){
        if (entity != null){
            LivingEntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
            if (entityPatch != null){
                return entityPatch.getTarget();
            }
            else if (entity instanceof Mob){
                return ((Mob)(entity)).getTarget();
            }
        }
        return null;
    }
}
