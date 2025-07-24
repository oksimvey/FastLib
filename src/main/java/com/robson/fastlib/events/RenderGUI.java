package com.robson.fastlib.events;

import com.robson.fastlib.api.data.manager.PlayerDataManager;
import com.robson.fastlib.api.data.types.PlayerData;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RenderGUI {

    private static final ResourceLocation targettexture = new ResourceLocation("fastlib", "textures/gui/target.png");
    private static FastVec3f lastRenderPoint = null;
    @SubscribeEvent
    public static void onRenderGUIEvent(RenderGuiOverlayEvent event) {
    }

}
