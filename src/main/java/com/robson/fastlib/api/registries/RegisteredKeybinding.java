package com.robson.fastlib.api.registries;

import com.robson.fastlib.api.keybinding.BasicKey;

import java.util.ArrayList;
import java.util.List;

public class RegisteredKeybinding {

    private static List<BasicKey> registeredKeys = new ArrayList<>();

    public static void registerKey(BasicKey keyBinding) {
        registeredKeys.add(keyBinding);
    }

    public static List<BasicKey> getRegisteredKeys() {
        return registeredKeys;
    }

}
