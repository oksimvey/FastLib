package com.robson.fastlib.api.data.manager;

import com.robson.fastlib.api.data.types.PlayerData;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerDataManager {

    private static ConcurrentHashMap<Player, PlayerData> CACHE = new ConcurrentHashMap<>();

    public static void init(Player player){

            CACHE.put(player, new PlayerData());

    }

    public static void remove(Player player){
        CACHE.remove(player);
    }

    public static PlayerData get(Player player){
        if (player == null)return null;
        return CACHE.get(player);
    }
}
