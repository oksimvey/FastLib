package com.robson.fastlib.api.events.types;

import com.mojang.blaze3d.vertex.PoseStack;
import com.robson.fastlib.api.events.FlaggedEvent;
import com.robson.fastlib.api.events.manager.FastLibEventManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public abstract class OnRenderPatchedEntityEvent extends FastLibEvent<OnRenderPatchedEntityEvent.Context> implements FlaggedEvent<Byte> {

    public static final FastLibEventManager<Context, OnRenderPatchedEntityEvent> EVENT_MANAGER = new FastLibEventManager<>();

    public record Context(LivingEntity entity, LivingEntityPatch<?> entityPatch, PoseStack poseStack, MultiBufferSource buffers,
                          float partialTicks, int light, Armature armature, OpenMatrix4f[] poses){ }
}
