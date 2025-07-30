package com.robson.fastlib.events;

import com.robson.fastlib.api.events.types.OnLivingAttack;
import com.robson.fastlib.api.events.types.OnLivingHurt;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LivingEntityEvents {

    @SubscribeEvent
    public static void onLivingEntityAttack(LivingAttackEvent event){
        OnLivingAttack.EVENT_MANAGER.shotEvents(event);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        OnLivingHurt.EVENT_MANAGER.shotEvents(event);
    }
}
