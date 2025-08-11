package com.robson.fastlib.api.events.manager;

import com.robson.fastlib.api.events.types.FastLibEvent;
import com.robson.fastlib.api.events.types.FlaggedEvent;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class FastLibEventManager<A, T extends FastLibEvent<A>> {

    private List<T> events = new ArrayList<>();

    public void registerEvent(T event){
        this.events.add(event);
    }

    public void shotEvents(A arg){
        for (FastLibEvent<A> event : events){
            if (event instanceof FlaggedEvent event1 && Minecraft.getInstance().player != null && Minecraft.getInstance().player.tickCount % event1.getFlag() != 0){
                continue;
            }
            if (event.canTick(arg)){
                event.onTick(arg);
            }
        }
    }
}
