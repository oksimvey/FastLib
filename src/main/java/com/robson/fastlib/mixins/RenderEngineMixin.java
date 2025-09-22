package com.robson.fastlib.mixins;

import net.minecraft.client.CameraType;
import net.minecraftforge.client.event.ViewportEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.events.engine.RenderEngine;

@Mixin(RenderEngine.class)
public class RenderEngineMixin {

    @Inject(method = "setRangedWeaponThirdPerson", at = @At("HEAD"), cancellable = true, remap = false)
    private void set(ViewportEvent.ComputeCameraAngles event, CameraType pov, double partialTicks, CallbackInfo ci){
        ci.cancel();
    }
}
