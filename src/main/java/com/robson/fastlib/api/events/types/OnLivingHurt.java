package com.robson.fastlib.api.events.types;

import com.robson.fastlib.api.events.manager.FastLibEventManager;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public abstract class OnLivingHurt extends FastLibEvent<LivingHurtEvent>{

    public static final FastLibEventManager<LivingHurtEvent, OnLivingHurt> EVENT_MANAGER = new FastLibEventManager<>();

}
