package com.robson.fastlib.mixins;

import com.robson.fastlib.api.data.manager.PlayerDataManager;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Shadow
    public Input input;

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/Tutorial;onInput(Lnet/minecraft/client/player/Input;)V"))
    private void onMovementInputUpdate(CallbackInfo ci) {
        if (input.getMoveVector().lengthSquared() == 0)return;
        var data = PlayerDataManager.get((LocalPlayer)(Object)this);
        if (data != null){
            data.getKeyHandler().handleKeyboardInput(input);
        }

    }
}
