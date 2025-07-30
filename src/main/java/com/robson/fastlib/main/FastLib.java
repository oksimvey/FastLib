package com.robson.fastlib.main;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.robson.fastlib.api.data.reloader.FastLibDataReloader;
import com.robson.fastlib.api.events.types.OnRenderEntityEvent;
import com.robson.fastlib.api.events.types.OnRenderGUIEvent;
import com.robson.fastlib.api.events.types.OnRenderPatchedEntityEvent;
import com.robson.fastlib.api.keybinding.BasicKey;
import com.robson.fastlib.api.keybinding.KeyBinding;
import com.robson.fastlib.api.registries.RegisteredKeybinding;
import com.robson.fastlib.api.utils.math.FastLibMathUtils;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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
