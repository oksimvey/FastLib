package com.robson.fastlib.main;

import com.robson.fastlib.events.ParticleRegister;
import com.robson.fastlib.fastlibevents.CameraEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("fastlib")
public class FastLib {

    public static final String MOD_ID = "fastlib";

    public FastLib(FMLJavaModLoadingContext context) {
        final IEventBus bus = context.getModEventBus();
        bus.addListener(this::init);
        ParticleRegister.PARTICLES.register(bus);
    }

    private void init(FMLCommonSetupEvent event) {
        event.enqueueWork(CameraEvents::registerEvent);
    }

}
