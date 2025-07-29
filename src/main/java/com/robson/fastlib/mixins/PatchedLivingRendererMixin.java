package com.robson.fastlib.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.robson.fastlib.api.events.types.OnRenderPatchedEntityEvent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(PatchedLivingEntityRenderer.class)
@OnlyIn(Dist.CLIENT)
public class PatchedLivingRendererMixin {

    @Inject(method = "renderLayer", at = @At(value = "HEAD"), remap = false)
    public void render(LivingEntityRenderer<?, ?> renderer, LivingEntityPatch<?> entitypatch, LivingEntity entity, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks, CallbackInfo ci){
        OnRenderPatchedEntityEvent.EVENT_MANAGER.shotEvents(new OnRenderPatchedEntityEvent.Context(entity, entitypatch, poseStack, buffer, partialTicks, packedLight, entitypatch.getArmature(), poses));
    }
}
