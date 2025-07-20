package com.robson.fastlib.api.registries;

import com.robson.fastlib.api.keybinding.KeyBinding;

import java.util.ArrayList;
import java.util.List;

public class RegisteredKeybinding {

    private static List<KeyBinding> registeredKeys = new ArrayList<>();

    public static void registerKey(KeyBinding keyBinding) {
        registeredKeys.add(keyBinding);
    }

    public static List<KeyBinding> getRegisteredKeys() {
        return registeredKeys;
    }

}
