package com.robson.fastlib.events;

import com.robson.fastlib.api.customtick.PlayerCustomTickManager;
import com.robson.fastlib.api.data.manager.PlayerDataManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerSetupEvents {

    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event){
        if (event.getEntity() != null){
           PlayerCustomTickManager.startTick(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event){
        if (event.getEntity() != null){
           PlayerCustomTickManager.stopTick(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onClonned(PlayerEvent.Clone event){
        if (event.isWasDeath()) {
            PlayerCustomTickManager.startRespawnTick(event.getEntity());
        }
    }
}
