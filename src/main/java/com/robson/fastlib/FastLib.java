package com.robson.fastlib;

import com.robson.fastlib.api.data.reloader.FastLibDataReloader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("fastlib")
public class FastLib {

    public static final String MOD_ID = "fastlib";

    public FastLib(FMLJavaModLoadingContext context) {
        MinecraftForge.EVENT_BUS.addListener(this::addReloadListenerEvent);
    }

    private void addReloadListenerEvent(final AddReloadListenerEvent event) {
        event.addListener(new FastLibDataReloader());
    }
}
