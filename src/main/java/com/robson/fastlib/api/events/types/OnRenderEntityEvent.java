package com.robson.fastlib.api.events.types;

import com.mojang.blaze3d.vertex.PoseStack;
import com.robson.fastlib.api.events.manager.FastLibEventManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;

public abstract class OnRenderEntityEvent extends FastLibEvent<OnRenderEntityEvent.Context> implements FlaggedEvent<Byte>{

    public static final FastLibEventManager<Context, OnRenderEntityEvent> EVENT_MANAGER = new FastLibEventManager<>();

    public record Context(LivingEntity entity, PoseStack poseStack, MultiBufferSource buffers, float partialTicks, int light){}

}
