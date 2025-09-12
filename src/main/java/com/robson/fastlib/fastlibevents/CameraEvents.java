package com.robson.fastlib.fastlibevents;

import com.robson.fastlib.api.events.manager.PlayerCustomTickManager;
import com.robson.fastlib.api.events.types.FastLibPlayerEvent;
import com.robson.fastlib.api.keybinding.KeyHandler;
import com.robson.fastlib.api.utils.Scheduler;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;

import java.util.concurrent.TimeUnit;

public class CameraEvents {

    public static void registerEvent() {

        PlayerCustomTickManager.EVENT_MANAGER.registerEvent(new FastLibPlayerEvent() {
            @Override
            public byte getFlag() {
                return 1;
            }

            @Override
            public boolean canTick(Context args) {
                return true;
            }

            @Override
            public void onTick(Context args) {
                args.playerData().getCamera().setDecoupled(!KeyHandler.isKeyDown(Minecraft.getInstance().options.keyUse));
            }
        });

        PlayerCustomTickManager.EVENT_MANAGER.registerEvent(new FastLibPlayerEvent() {


            @Override
            public boolean canTick(Context args) {
                return true;
            }

            @Override
            public void onTick(Context args) {
                final Vec3 oldpos = args.player().position();
                Scheduler.schedule(()-> {
                    FastVec3f delta = new FastVec3f((float) (args.player().getX() - oldpos.x), (float) (args.player().getY() - oldpos.y), (float) (args.player().getZ() - oldpos.z));
                    float zmodifier =  0f;
                    float tomodifyz = 1;
                    for (int i = 0; i < 10; i++) {
                        if (args.player().level().getBlockState(args.player().blockPosition().offset(0, i, 0)).isSolid()) {
                            tomodifyz = zmodifier * (1f / (minimum(i))) * 10;
                            zmodifier -= tomodifyz;
                            break;
                        }
                    }
                    zmodifier += tomodifyz * delta.length();
                    RenderItemBase render = ClientEngine.getInstance().renderEngine.getItemRenderer(args.player().getMainHandItem());
                    if (render != null){
                        TrailInfo info = render.trailInfo();
                        if (info != null){
                            zmodifier += (float) info.start().subtract(info.end()).length() * 0.15f * tomodifyz;
                        }
                    }
                    float xmodifier = -0.5f + (zmodifier / 100);
                    float ymodifier = -0.05f + (zmodifier / 5);
                    args.playerData().getCamera().setPos(new FastVec3f(xmodifier, ymodifier, -zmodifier));
                }, 100, TimeUnit.MILLISECONDS);

            }

            @Override
            public byte getFlag() {
                return 5;
            }
        });
    }


    static int minimum(int i){
        return i == 0 ? 1 : i;
    }
}
