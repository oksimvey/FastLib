package com.robson.fastlib.fastlibevents;

import com.robson.fastlib.api.camera.CustomCam;
import com.robson.fastlib.api.data.manager.PlayerDataManager;
import com.robson.fastlib.api.events.manager.PlayerCustomTickManager;
import com.robson.fastlib.api.events.types.FastLibPlayerEvent;
import com.robson.fastlib.api.keybinding.BasicKey;
import com.robson.fastlib.api.keybinding.KeyHandler;
import com.robson.fastlib.api.registries.RegisteredKeybinding;
import com.robson.fastlib.api.utils.Scheduler;
import com.robson.fastlib.api.utils.math.FastVec3f;
import com.robson.fastlib.events.RegisterKeybinding;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.SkillDataKeys;
import yesman.epicfight.skill.SkillDataManager;

import java.util.concurrent.TimeUnit;

public class CameraEvents {

    public static void registerEvent() {
        RegisteredKeybinding.registerKey(new BasicKey(RegisterKeybinding.LOCK_ON) {
            @Override
            public boolean shouldHandle(Player player) {
                return true;
            }

            @Override
            public void onPress(Player player) {
                CustomCam cam = PlayerDataManager.get(player).getCamera();
                if (cam.getTarget() == null) {
                    cam.selectNearestTarget(player, player.position(), 50, false);
                }
                else cam.setTarget(null);
            }
        });

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
                boolean intial = !KeyHandler.isKeyDown(Minecraft.getInstance().options.keyUse);
                if (intial) {
                   intial = !(args.playerPatch().getEntityState().attacking()) || args.player().getVehicle() != null;
                }
                args.playerData().getCamera().setDecoupled(intial);
            }
        });

        PlayerCustomTickManager.EVENT_MANAGER.registerEvent(new FastLibPlayerEvent() {

            @Override
            public boolean canTick(Context args) {
                return !args.playerData().getCamera().isCutsceneActive();
            }

            @Override
            public void onTick(Context args) {
                final Vec3 oldpos = args.player().position();
                Scheduler.schedule(() -> {
                    FastVec3f delta = new FastVec3f((float) (args.player().getX() - oldpos.x), (float) (args.player().getY() - oldpos.y), (float) (args.player().getZ() - oldpos.z));
                    float zmodifier = 0f;
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
                    if (render != null) {
                        TrailInfo info = render.trailInfo();
                        if (info != null) {
                            zmodifier += (float) info.start().subtract(info.end()).length() * 0.15f * tomodifyz;
                        }
                    }
                    SkillDataManager dataManager = args.playerPatch().getSkill(EpicFightSkills.BASIC_ATTACK).getDataManager();
                    int comboCounter = (Integer) dataManager.getDataValue((SkillDataKey) SkillDataKeys.COMBO_COUNTER.get());
                        zmodifier += 0.1f * comboCounter;
                        boolean istargeting = args.playerData().getCamera().getTarget() != null;
                    if (args.player().getVehicle() != null) {
                        zmodifier += args.player().getVehicle().getBbHeight() / 5;
                    }
                    float xmodifier = istargeting ? 0 : -0.5f ;
                    float ymodifier = -0.05f + (zmodifier / 5);
                    if (istargeting) {
                        ymodifier += 0.75f + (args.playerData().getCamera().getTarget().getBbHeight() / 10);
                        zmodifier += 0.25f + (args.playerData().getCamera().getTarget().getBbHeight() / 10);
                    }
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
