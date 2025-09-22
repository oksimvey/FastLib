package com.robson.fastlib.fastlibevents;

import com.robson.fastlib.api.camera.CustomCam;
import com.robson.fastlib.api.data.manager.PlayerDataManager;
import com.robson.fastlib.api.events.manager.PlayerCustomTickManager;
import com.robson.fastlib.api.events.types.FastLibPlayerEvent;
import com.robson.fastlib.api.keybinding.BasicKey;
import com.robson.fastlib.api.keybinding.KeyHandler;
import com.robson.fastlib.api.registries.RegisteredKeybinding;
import com.robson.fastlib.api.utils.Scheduler;
import com.robson.fastlib.api.utils.TargetUtils;
import com.robson.fastlib.api.utils.VfxUtils;
import com.robson.fastlib.api.utils.math.FastLibMathUtils;
import com.robson.fastlib.api.utils.math.FastVec3f;
import com.robson.fastlib.events.ParticleRegister;
import com.robson.fastlib.events.RegisterKeybinding;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.SkillDataKeys;
import yesman.epicfight.skill.SkillDataManager;

import java.util.ArrayList;
import java.util.List;
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
                } else cam.setTarget(null);
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
                    FastVec3f delta = new FastVec3f(
                            (float) (args.player().getX() - oldpos.x),
                            (float) (args.player().getY() - oldpos.y),
                            (float) (args.player().getZ() - oldpos.z));

                    float zmodifier = -1f;
                    float tomodifyz = 1;
                    for (int i = 1; i < 10; i++) {
                        if (args.player().level().getBlockState(args.player().blockPosition().offset(0, i, 0)).isSolid()) {
                            tomodifyz = zmodifier * (1f / (minimum(i))) * 5;
                            zmodifier -= tomodifyz;
                            break;
                        }
                    }
                    short angle = (short) Mth.wrapDegrees(args.playerData().getKeyHandler().getKeyboardInput().getInputAngle() + (args.playerData().getCamera().getMc().cameraEntity.getYRot()));
                    FastVec3f rotated = delta.rotate(angle);
                    args.playerData().getCamera().setRoll(rotated.x() * 5f);

                    zmodifier += (float) (tomodifyz * Math.min(5, Math.exp(delta.length() * 0.25)));
                    RenderItemBase render = ClientEngine.getInstance().renderEngine.getItemRenderer(args.player().getMainHandItem());
                    if (render != null) {
                        TrailInfo info = render.trailInfo();
                        if (info != null) {
                            zmodifier += (float) info.start().subtract(info.end()).length() * 0.2f * tomodifyz;
                        }
                    }
                    SkillDataManager dataManager = args.playerPatch().getSkill(EpicFightSkills.BASIC_ATTACK).getDataManager();
                    int comboCounter = (Integer) dataManager.getDataValue((SkillDataKey) SkillDataKeys.COMBO_COUNTER.get());
                    zmodifier += 0.15f * comboCounter;
                    boolean istargeting = args.playerData().getCamera().getTarget() != null;
                    if (args.player().getVehicle() != null) {
                        zmodifier += args.player().getVehicle().getBbHeight() / 5;
                    }
                    float xmodifier = istargeting ? 0 : -0.5f - (zmodifier / 20);
                    float ymodifier = 0.05f + (zmodifier / 40);
                    if (istargeting) {
                        ymodifier += 0.75f + (args.playerData().getCamera().getTarget().getBbHeight() / 10);
                        zmodifier += 0.25f + (args.playerData().getCamera().getTarget().getBbHeight() / 10);
                    }

                    List<LivingEntity> targets = new ArrayList<>();
                    for (Entity ent : args.player().level().getEntities(args.player(), FastLibMathUtils.createAABBAroundEnt(args.player(), 25))) {
                        if (ent instanceof LivingEntity && ent != args.playerData().getCamera().getTarget() && TargetUtils.getTarget((LivingEntity) ent) == args.player()) {
                            targets.add((LivingEntity) ent);
                        }
                    }

                    if (!targets.isEmpty()) {
                        float targetingSizeModifier = 0;
                        byte targetingEntities = (byte) targets.size();
                        for (Entity ent : targets) {
                            if (ent != null && ent.getBbHeight() / 5 > targetingSizeModifier) {
                                targetingSizeModifier = ent.getBbHeight() / 5;
                            }
                        }
                        if (targetingEntities > 5) {
                            targetingEntities = 5;
                        }
                        ymodifier += (targetingSizeModifier / 20) + (targetingEntities / 100f);
                        zmodifier += targetingSizeModifier + targetingEntities / 5f;


                    }
                    args.playerData().getCamera().setPos(new FastVec3f(xmodifier, ymodifier, -zmodifier));
                }, 250, TimeUnit.MILLISECONDS);

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
