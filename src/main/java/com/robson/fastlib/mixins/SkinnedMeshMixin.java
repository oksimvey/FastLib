package com.robson.fastlib.mixins;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import yesman.epicfight.api.client.model.SkinnedMesh;

@OnlyIn(Dist.CLIENT)
@Mixin(SkinnedMesh.class)
public class SkinnedMeshMixin {
}
