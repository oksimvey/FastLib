package com.robson.fastlib.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.robson.fastlib.api.events.types.OnRenderEntityEvent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
@OnlyIn(Dist.CLIENT)
public class LivingEntityRendererMixin {



    @Inject(method = "render*", at = @At("HEAD"))
    public void renderI(LivingEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int light, CallbackInfo info) {
        OnRenderEntityEvent.EVENT_MANAGER.shotEvents(new OnRenderEntityEvent.Context(entity, poseStack, buffers, partialTicks, light));
    }
}
