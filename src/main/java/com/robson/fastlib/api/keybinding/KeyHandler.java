package com.robson.fastlib.api.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import com.robson.fastlib.api.registries.RegisteredKeybinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

public class KeyHandler {

    private final IInput mouseInput;

    private final IInput keyboardInput;

    public KeyHandler(){
        mouseInput = new IInput();
        keyboardInput = new IInput();
    }

    public void tick(Player player) {
        if (player != null && Minecraft.getInstance().screen == null) {
            handleKeyboardInput();
           for (KeyBinding keyBinding : RegisteredKeybinding.getRegisteredKeys()){
               if (keyBinding.shouldHandle(player)){
                   handleKeyInput(player, keyBinding.getKeyMapping(), keyBinding.getKey());
               }
           }
        }
    }

    void handleKeyboardInput(){
        Options options = Minecraft.getInstance().options;
        keyboardInput.setUp(isKeyDown(options.keyUp));
        keyboardInput.setDown(isKeyDown(options.keyDown));
        keyboardInput.setLeft(isKeyDown(options.keyLeft));
        keyboardInput.setRight(isKeyDown(options.keyRight));
    }

    void handleMouseInput(){

    }


    static void handleKeyInput(Player player, KeyMapping key, BasicKey keyAction) {
        if (isKeyDown(key)) {
            keyAction.onPressTick(player);
            return;
        }
        if (keyAction.isPressed()) {
            keyAction.onRelease(player);
        }
    }

    public static boolean isKeyDown(KeyMapping key) {
        if (key.getKey().getType() == InputConstants.Type.KEYSYM) {
            return key.isDown() || GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), key.getKey().getValue()) > 0;
        } else if (key.getKey().getType() != InputConstants.Type.MOUSE) {
            return false;
        } else {
            return key.isDown() || GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), key.getKey().getValue()) > 0;
        }
    }

    public IInput getMouseInput(){
        return mouseInput;
    }

    public IInput getKeyboardInput(){
        return keyboardInput;
    }

}
