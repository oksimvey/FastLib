package com.robson.fastlib.main;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("fastlib")
public class FastLib {

    public static final String MOD_ID = "fastlib";

    public FastLib(FMLJavaModLoadingContext context) {
        context.getModEventBus().addListener(this::init);
    }

    private void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }
}
