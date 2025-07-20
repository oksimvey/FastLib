package com.robson.fastlib.mixins;

import com.robson.fastlib.api.data.manager.PlayerDataManager;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
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


    @Shadow @Final private Minecraft minecraft;

    @Shadow private double accumulatedDX;

    @Shadow private double accumulatedDY;

    @Inject(method = "turnPlayer", at = @At(value = "HEAD"))
    private void onTurnStart(CallbackInfo ci) {
        var data = PlayerDataManager.get(this.minecraft.player);
        if (data == null) return;
        data.getCamera().handleMouseMovement(this.minecraft.player, this.accumulatedDX, this.accumulatedDY);
    }

    @Inject(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), cancellable = true)
    private void onTurn(CallbackInfo ci) {
        if (Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON) {
            ci.cancel();
        }
    }
}