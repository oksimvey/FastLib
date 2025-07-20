package com.robson.fastlib.main;

import com.robson.fastlib.api.camera.CustomCam;
import com.robson.fastlib.api.camera.Cutscene;
import com.robson.fastlib.api.data.manager.PlayerDataManager;
import com.robson.fastlib.api.data.reloader.FastLibDataReloader;
import com.robson.fastlib.api.keybinding.BasicKey;
import com.robson.fastlib.api.keybinding.KeyBinding;
import com.robson.fastlib.api.registries.RegisteredKeybinding;
import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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
                            CustomCam cam = PlayerDataManager.get(player).getCamera();
                            if (cam.getSelected() != null){
                                cam.setSelected(null);
                                return;
                            }
                            float distance = 50;
                            for (Entity entity : player.level().getEntities(player, player.getBoundingBox().inflate(10))) {
                                if (entity instanceof Mob && entity.distanceTo(player) < distance){
                                    distance = entity.distanceTo(player);
                                    cam.setSelected((LivingEntity) entity);
                                }
                            }
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
                        CustomCam cam = PlayerDataManager.get(player).getCamera();
                        cam.setCutscene(
                                new Cutscene(2000,
                                        new Cutscene.CameraKeyFrame(new FastVec3f(-2, 0.5f, 0), new FastVec2f(2, 3)),
                        new Cutscene.CameraKeyFrame(new FastVec3f(0, 0.6f, 2), new FastVec2f(2, 3)),
                        new Cutscene.CameraKeyFrame(new FastVec3f(2, 0.87f, 0), new FastVec2f(2, 3)),
                        new Cutscene.CameraKeyFrame(new FastVec3f(0, 0.8f, -2), new FastVec2f(2, 3))));
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
