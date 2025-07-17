package com.robson.fastlib.api.customtick;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerCustomTickManager {

    private static List<PlayerCustomTickEvent> EVENTS = new ArrayList<>();

    public static void registerEvent(PlayerCustomTickEvent event){
      EVENTS.add(event);
    }

    public static void handle(Player player){
        EVENTS.forEach(event -> event.onTick(player));
    }
}
