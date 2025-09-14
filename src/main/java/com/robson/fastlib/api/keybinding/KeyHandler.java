package com.robson.fastlib.api.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import com.robson.fastlib.api.registries.RegisteredKeybinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

public class KeyHandler {

    private final InputHandler mouseInput;

    private final InputHandler keyboardInput;

    public KeyHandler(){
        mouseInput = new InputHandler();
        keyboardInput = new InputHandler();
    }

    public void tick(Player player) {
        if (player != null && Minecraft.getInstance().screen == null) {
           for (BasicKey keyBinding : RegisteredKeybinding.getRegisteredKeys()){
               if (keyBinding.shouldHandle(player)){
                   handleKeyInput(player, keyBinding.getKeyMapping(), keyBinding);
               }
           }
        }
    }

   public void handleKeyboardInput(Input input){
        keyboardInput.setUp(input.up);
        keyboardInput.setDown(input.down);
        keyboardInput.setLeft(input.left);
        keyboardInput.setRight(input.right);
    }

    public void handleMouseInput(float dx, float dy) {
        mouseInput.setUp(dx < 0);
        mouseInput.setDown(dx > 0);
        mouseInput.setLeft(dy < 0);
        mouseInput.setRight(dy > 0);
    }

    public InputHandler getMouseInput(){
        return mouseInput;
    }

    public InputHandler getKeyboardInput(){
        return keyboardInput;
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
        }
        else if (key.getKey().getType() != InputConstants.Type.MOUSE) {
            return false;
        }
        else {
            return key.isDown() || GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), key.getKey().getValue()) > 0;
        }
    }
}
