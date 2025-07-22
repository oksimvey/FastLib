package com.robson.fastlib.api.customtick;

import com.robson.fastlib.api.data.manager.PlayerDataManager;
import com.robson.fastlib.api.utils.Scheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerCustomTickManager {

    private static List<PlayerCustomTickEvent> EVENTS = new ArrayList<>();

    public static void registerEvent(PlayerCustomTickEvent event){
      EVENTS.add(event);
    }

    public static void handle(Player player) {
        EVENTS.forEach(event -> {
            if (player.tickCount % event.getFlag() == 0 && event.canTick(player)) {
                event.onTick(player);
            }
        });
    }

    public static void startTick(Player player) {
        stopTick(player);
        PlayerDataManager.init(player);
        loopTick(player);
    }

    public static void stopTick(Player player) {
        PlayerDataManager.remove(player);
    }

    public static void onTick(Player player) {
        if (PlayerDataManager.get(player) != null) {
            loopTick(player);
            if (!Minecraft.getInstance().isPaused()) {
                PlayerDataManager.get(player).tick(player);
                handle(player);
            }
        }
    }

    public static void startRespawnTick(Player player) {
        stopTick(player);
        Scheduler.schedule(() -> startTick(player), 52, TimeUnit.MILLISECONDS);
    }

    public static void loopTick(Player player) {
        Scheduler.schedule(() -> onTick(player), 50, TimeUnit.MILLISECONDS);
    }
}
