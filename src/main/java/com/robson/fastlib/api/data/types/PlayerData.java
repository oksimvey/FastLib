package com.robson.fastlib.api.data.types;

import com.robson.fastlib.api.camera.CustomCam;
import com.robson.fastlib.api.keybinding.KeyHandler;
import net.minecraft.world.entity.player.Player;

public class PlayerData {


    private final CustomCam camera;

    private final KeyHandler keyHandler;

    public PlayerData() {
        this.camera = new CustomCam();
        this.keyHandler = new KeyHandler();
    }

    public CustomCam getCamera() {
        return camera;
    }

    public void tick(Player player){
        keyHandler.tick(player);
    }

}
