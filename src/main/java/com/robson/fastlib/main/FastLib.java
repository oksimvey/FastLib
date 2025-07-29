package com.robson.fastlib.main;

import com.mojang.blaze3d.vertex.PoseStack;
import com.robson.fastlib.api.data.reloader.FastLibDataReloader;
import com.robson.fastlib.api.events.types.OnRenderPatchedEntityEvent;
import com.robson.fastlib.api.keybinding.BasicKey;
import com.robson.fastlib.api.keybinding.KeyBinding;
import com.robson.fastlib.api.registries.RegisteredKeybinding;
import com.robson.fastlib.api.utils.math.FastLibMathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.gameasset.Armatures;

@Mod("fastlib")
public class FastLib {

    public static final String MOD_ID = "fastlib";

    public FastLib(FMLJavaModLoadingContext context) {
        MinecraftForge.EVENT_BUS.addListener(this::addReloadListenerEvent);
        context.getModEventBus().addListener(this::init);
    }

    private void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            OnRenderPatchedEntityEvent.EVENT_MANAGER.registerEvent(new OnRenderPatchedEntityEvent() {

                @Override
                public boolean canTick(Context args) {
                    return args.entity() instanceof Player;
                }

                @Override
                public void onTick(Context args) {
                    ItemStack itemStack = Items.NETHERITE_SWORD.getDefaultInstance();
                    PoseStack stack = FastLibMathUtils.correctPoseStack(args.poseStack(),args.poses()[Armatures.BIPED.get().rootJoint.getId()]);
                    Minecraft.getInstance().getItemRenderer().render(itemStack,
                            ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, true, stack, args.buffers(), args.light(), args.light(),
                            Minecraft.getInstance().getItemRenderer().getModel(itemStack, Minecraft.getInstance().level, args.entity(), args.light()));
                }

                @Override
                public Byte getFlag() {
                    return 1;
                }
            });
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
