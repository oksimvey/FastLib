package com.robson.fastlib.api.events.manager;

import com.robson.fastlib.api.data.manager.PlayerDataManager;
import com.robson.fastlib.api.events.types.FastLibEvent;
import com.robson.fastlib.api.utils.Scheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.TimeUnit;

public interface PlayerCustomTickManager{

    FastLibEventManager<Player, FastLibEvent<Player>> EVENT_MANAGER = new FastLibEventManager<>();

    static void handle(Player player) {
        EVENT_MANAGER.shotEvents(player);
    }

    static void startTick(Player player) {
        stopTick(player);
        PlayerDataManager.init(player);
        loopTick(player);
    }

    static void stopTick(Player player) {
        PlayerDataManager.remove(player);
    }

    static void onTick(Player player) {
        if (PlayerDataManager.get(player) != null) {
            loopTick(player);
            if (!Minecraft.getInstance().isPaused()) {
                PlayerDataManager.get(player).tick(player);
                handle(player);
            }
        }
    }

    static void startRespawnTick(Player player) {
        stopTick(player);
        Scheduler.schedule(() -> startTick(player), 52, TimeUnit.MILLISECONDS);
    }

    static void loopTick(Player player) {
        Scheduler.schedule(() -> onTick(player), 50, TimeUnit.MILLISECONDS);
    }
}
