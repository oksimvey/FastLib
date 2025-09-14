package com.robson.fastlib.api.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private static List<FastlibConfig> commonconfig = new ArrayList<>();

    public static void registerConfig(FastlibConfig config) {
        commonconfig.add(config);
    }

    public static List<FastlibConfig> getRegisteredConfigs() {
        return commonconfig;
    }

    @OnlyIn(Dist.CLIENT)
    private static List<FastlibConfig> clientconfig = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    public static void registerClientConfig(FastlibConfig config) {
        clientconfig.add(config);
    }

    @OnlyIn(Dist.CLIENT)
    public static List<FastlibConfig> getClientRegisteredConfigs() {
        return clientconfig;
    }



}
