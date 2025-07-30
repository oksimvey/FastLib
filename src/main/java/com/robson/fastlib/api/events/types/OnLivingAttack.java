package com.robson.fastlib.api.events.types;

import com.robson.fastlib.api.events.manager.FastLibEventManager;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public abstract class OnLivingAttack extends FastLibEvent<LivingAttackEvent> {

    public static final FastLibEventManager<LivingAttackEvent, OnLivingAttack> EVENT_MANAGER = new FastLibEventManager<>();

}
