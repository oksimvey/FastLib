package com.robson.fastlib.main;

import com.robson.fastlib.api.data.reloader.FastLibDataReloader;
import com.robson.fastlib.api.keybinding.BasicKey;
import com.robson.fastlib.api.keybinding.KeyBinding;
import com.robson.fastlib.api.registries.RegisteredKeybinding;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("fastlib")
public class FastLib {

    public static final String MOD_ID = "fastlib";

    public FastLib(FMLJavaModLoadingContext context) {
        MinecraftForge.EVENT_BUS.addListener(this::addReloadListenerEvent);
        context.getModEventBus().addListener(this::init);
    }

    private void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            RegisteredKeybinding.registerKey(new KeyBinding(
                    Minecraft.getInstance().options.keyAttack,
                    new BasicKey() {

                        @Override
                        public void onPress(Player player) {


                        }
                    }
            ) {
                @Override
                public boolean shouldHandle(Player player) {
                    return player.getMainHandItem().getItem() instanceof SwordItem;
                }
            });
         });
        RegisteredKeybinding.registerKey(new KeyBinding(
                Minecraft.getInstance().options.keyUse,
                new BasicKey() {

                    @Override
                    public void onPress(Player player) {

                    }
                }
        ) {
            @Override
            public boolean shouldHandle(Player player) {
                return true;
            }
        });
    }

    private void addReloadListenerEvent(final AddReloadListenerEvent event) {
        event.addListener(new FastLibDataReloader());
    }
}
