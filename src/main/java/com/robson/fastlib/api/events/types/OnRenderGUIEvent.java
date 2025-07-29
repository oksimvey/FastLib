package com.robson.fastlib.api.events.types;

import com.robson.fastlib.api.events.manager.FastLibEventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;

public abstract class OnRenderGUIEvent extends FastLibEvent<OnRenderGUIEvent.Context>{

    public static final FastLibEventManager<Context, OnRenderGUIEvent> EVENT_MANAGER = new FastLibEventManager<>();

    public record Context(GuiGraphics guiGraphics, float partialTicks, LocalPlayer player, Minecraft minecraft, int screenHeight, int screenWidth){}
}
