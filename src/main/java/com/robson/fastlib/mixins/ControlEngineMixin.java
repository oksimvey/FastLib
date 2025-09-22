package com.robson.fastlib.mixins;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import yesman.epicfight.client.events.engine.ControlEngine;

@OnlyIn(Dist.CLIENT)
@Mixin(ControlEngine.class)
public class ControlEngineMixin {
}
