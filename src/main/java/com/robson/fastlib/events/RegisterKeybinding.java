package com.robson.fastlib.events;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

public class RegisterKeybinding {

     public static KeyMapping LOCK_ON;

        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            LOCK_ON = new KeyMapping("key.fastlib.special", InputConstants.Type.MOUSE, 2, "key.categories.misc");
            event.register(LOCK_ON);
        }
}
