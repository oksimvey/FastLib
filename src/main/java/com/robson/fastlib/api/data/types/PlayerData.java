package com.robson.fastlib.api.data.types;

import com.robson.fastlib.api.camera.CustomCam;
import com.robson.fastlib.api.keybinding.KeyHandler;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class PlayerData {

    private final CustomCam camera;

    private final KeyHandler keyHandler;

    public PlayerData() {
        this.camera = new CustomCam(0, 0, new FastVec3f(-0.25f, 0.5f, -1));
        this.keyHandler = new KeyHandler();
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public CustomCam getCamera() {
        return camera.isEnabled() ? camera : null;
    }

    public void tick(Player player){
        keyHandler.tick(player);
    }

}
