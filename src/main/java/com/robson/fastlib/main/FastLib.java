package com.robson.fastlib.main;

import com.robson.fastlib.api.camera.TargetMarker;
import com.robson.fastlib.api.config.ConfigManager;
import com.robson.fastlib.api.config.FastlibConfig;
import com.robson.fastlib.api.registries.RegisteredKeybinding;
import com.robson.fastlib.events.ParticleRegister;
import com.robson.fastlib.events.RegisterKeybinding;
import com.robson.fastlib.fastlibevents.CameraEvents;
import com.robson.fastlib.fastlibevents.GUIEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.client.gui.EntityUI;

@Mod("fastlib")
public class FastLib {

    public static final String MOD_ID = "fastlib";

    public FastLib(FMLJavaModLoadingContext context) {
        final IEventBus bus = context.getModEventBus();
        bus.addListener(this::init);
        bus.addListener(this::initClient);
        bus.addListener(RegisterKeybinding::registerKeyMappings);
        ParticleRegister.PARTICLES.register(bus);
    }

    private void initClient(FMLClientSetupEvent event){
        EntityUI.ENTITY_UI_LIST.add(TargetMarker.Instance);
        event.enqueueWork(()->{
            GUIEvents.registerEvents();
            for (FastlibConfig config : ConfigManager.getClientRegisteredConfigs()){
                config.read();
            }
        });
    }

    private void init(FMLCommonSetupEvent event) {
        event.enqueueWork(()-> {
            for (FastlibConfig config : ConfigManager.getRegisteredConfigs()){
                config.read();
            }
            CameraEvents.registerEvent();
        });
    }

}
