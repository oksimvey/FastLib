package com.robson.fastlib.events;

import com.robson.fastlib.api.events.manager.PlayerCustomTickManager;
import com.robson.fastlib.api.utils.VfxUtils;
import com.robson.fastlib.api.utils.math.FastLibMathUtils;
import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

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
