package com.robson.fastlib.api.data.types;

import com.robson.fastlib.api.camera.CustomCam;
import com.robson.fastlib.api.keybinding.KeyHandler;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class PlayerData {

    private final CustomCam camera;

    private final KeyHandler keyHandler;

    public PlayerData() {
        this.camera = new CustomCam(0, 0, new FastVec3f(0, 0, 0));
        this.keyHandler = new KeyHandler();
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public CustomCam getCamera() {
        return  camera ;
    }

    public void tick(Player player){
        keyHandler.tick(player);
    }

}
