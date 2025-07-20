package com.robson.fastlib.events;

import com.robson.fastlib.api.data.manager.PlayerDataManager;
import com.robson.fastlib.api.data.types.PlayerData;
import com.robson.fastlib.api.utils.math.FastLibMathUtils;
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
        PlayerData data = PlayerDataManager.get(Minecraft.getInstance().player);
        if (data == null || data.getCamera() == null) return;

        FastVec3f focusPoint = data.getCamera().getFocusPoint();
        if (focusPoint == null) {
            lastRenderPoint = null;
            return;
        }

        FastVec3f targetRenderPoint = FastLibMathUtils.transformWorldToScreen(focusPoint);

        if (lastRenderPoint == null) {
            lastRenderPoint = targetRenderPoint;
        }

        float lerpFactor = 0.3f;
        float smoothedX = Mth.lerp(lerpFactor, lastRenderPoint.x(), targetRenderPoint.x());
        float smoothedY = Mth.lerp(lerpFactor, lastRenderPoint.y(), targetRenderPoint.y());

        lastRenderPoint = new FastVec3f(smoothedX, smoothedY, 0);

        int iconSize = 10;
        event.getGuiGraphics().blit(
                targettexture,
                (int) smoothedX - (iconSize / 2),
                (int) smoothedY - (iconSize / 2),
                0, 0, iconSize, iconSize, iconSize, iconSize
        );  }

}
