package com.robson.fastlib.mixins;

import com.robson.fastlib.api.events.types.OnRenderGUIEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
@OnlyIn(Dist.CLIENT)
public class GUIMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void renderGUI(GuiGraphics guiGraphics, float partialticks, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            OnRenderGUIEvent.EVENT_MANAGER.shotEvents(new OnRenderGUIEvent.Context(guiGraphics, partialticks, minecraft.player, minecraft, minecraft.getWindow().getGuiScaledHeight(), minecraft.getWindow().getGuiScaledWidth()));
        }
    }
}

