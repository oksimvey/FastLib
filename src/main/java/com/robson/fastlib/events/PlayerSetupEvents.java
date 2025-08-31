package com.robson.fastlib.events;

import com.robson.fastlib.api.events.manager.PlayerCustomTickManager;
import com.robson.fastlib.api.utils.VfxUtils;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
    public static void onPLayerINteract(PlayerInteractEvent.RightClickItem event){
        VfxUtils.spawnBlockBreakParticle(event.getEntity(), 2);
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
