package com.robson.fastlib.events;

import com.robson.fastlib.api.events.types.OnRenderGUIEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
@OnlyIn(Dist.CLIENT)
public class OnRenderGUI {

    @SubscribeEvent
    public static void onRenderGUI(RenderGuiOverlayEvent event) {
        OnRenderGUIEvent.EVENT_MANAGER.shotEvents(new OnRenderGUIEvent.Context(event.getGuiGraphics(),
                event.getPartialTick(), Minecraft.getInstance().player, Minecraft.getInstance(), event.getWindow().getGuiScaledHeight(),
                event.getWindow().getGuiScaledWidth()));
    }
}
