package com.robson.fastlib.mixins;

import com.mojang.blaze3d.Blaze3D;
import com.robson.fastlib.api.data.types.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.SmoothDouble;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
@OnlyIn(Dist.CLIENT)
public abstract class MouseHandlerMixin {

    private static float lasrota = 0;

    @Shadow private double accumulatedDX;

    @Shadow private double accumulatedDY;

    @Shadow private double lastMouseEventTime;

    @Shadow public abstract boolean isMouseGrabbed();

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private SmoothDouble smoothTurnX;

    @Shadow @Final private SmoothDouble smoothTurnY;

    @Inject(
                method = {"turnPlayer()V"},
                at = {@At("HEAD")},
                cancellable = true
        )
        private void preTurnPlayer(CallbackInfo ci) {
        PlayerData.acumulateddx = (float) accumulatedDX;
        PlayerData.accumulateddy = (float) accumulatedDY;

    }}