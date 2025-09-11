package com.robson.fastlib.fastlibevents;

import com.robson.fastlib.api.events.manager.PlayerCustomTickManager;
import com.robson.fastlib.api.events.types.FastLibPlayerEvent;
import com.robson.fastlib.api.keybinding.KeyHandler;
import com.robson.fastlib.api.utils.Scheduler;
import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.concurrent.TimeUnit;

public class CameraEvents {

    public static void registerEvent(){

        PlayerCustomTickManager.EVENT_MANAGER.registerEvent(new FastLibPlayerEvent() {


            @Override
            public boolean canTick(Context args) {
                return true;
            }

            @Override
            public void onTick(Context args) {
                boolean decoupled = !KeyHandler.isKeyDown(Minecraft.getInstance().options.keyUse);
                args.playerData().getCamera().setDecoupled(decoupled);
                final Vec3 oldpos = args.player().position();
                Scheduler.schedule(()-> {
                    FastVec2f delta = new FastVec2f((float) (args.player().getX() - oldpos.x), (float) (args.player().getZ() - oldpos.z));
                    float zmodifier =  0.2f + (delta.length());
                    for (int i = 0; i < 10; i++) {
                        if (args.player().level().getBlockState(args.player().blockPosition().offset(0, i, 0)).isSolid()) {
                            zmodifier -= zmodifier * (1f / (minimum(i) * 0.15f));
                            break;
                        }
                    }
                    float xmodifier = -0.45f + (zmodifier / 100);
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
