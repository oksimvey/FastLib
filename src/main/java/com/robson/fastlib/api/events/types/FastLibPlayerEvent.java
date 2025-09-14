package com.robson.fastlib.api.events.types;

import com.robson.fastlib.api.data.types.PlayerData;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public abstract class FastLibPlayerEvent extends FastLibEvent<FastLibPlayerEvent.Context> implements FlaggedEvent {

    public record Context(Player player, PlayerData playerData, PlayerPatch<Player> playerPatch) {}
}
