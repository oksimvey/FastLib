package com.robson.fastlib.mixins;

import net.minecraft.client.CameraType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CameraType.class, priority = 1001)
@OnlyIn(Dist.CLIENT)
public class CameraTypeMixin {

    @Inject(method = "cycle", at = @At("RETURN"), cancellable = true)
    private void modifyCycle(CallbackInfoReturnable<CameraType> ci) {
        if (ci.getReturnValue() == CameraType.THIRD_PERSON_FRONT) {
            ci.setReturnValue(CameraType.FIRST_PERSON);
        }
    }
}
