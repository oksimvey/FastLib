package com.robson.fastlib.api.data.manager;

import com.robson.fastlib.api.data.types.PlayerData;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    private static ConcurrentHashMap<Player, PlayerData> PLAYER_DATA = new ConcurrentHashMap<>();

    public static void init(Player player){
        PLAYER_DATA.put(player, new PlayerData());
    }

    public static void remove(Player player){
        PLAYER_DATA.remove(player);
    }

    public static PlayerData get(Player player){
        if (player == null)return null;
        return PLAYER_DATA.get(player);
    }
}
