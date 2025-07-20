package com.robson.fastlib.api.camera;


import com.robson.fastlib.api.utils.math.FastLibMathUtils;
import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CustomCam {

    private Cutscene currentscene;

    protected float prevRot = 0;

    private FastVec3f currentOffset = FastVec3f.ZERO;
    private FastVec2f currentRotation = FastVec2f.ZERO;

    private FastVec3f targetOffset = FastVec3f.ZERO;
    private FastVec2f targetRotation = FastVec2f.ZERO;

    private final float lerpSpeed = 0.2f;

    private FastVec3f focusPoint;

    private LivingEntity selected;

    protected FastVec2f angles = new FastVec2f(0, 0);

    private List<FastVec2f> interpolatedrotation = new ArrayList<>();

    private int intialtickinterpolation = 0;

    private volatile boolean decoupled = true;

    public boolean disableRearCamera() {
        return true;
    }

    public FastVec3f getFocusPoint() {
        return focusPoint;
    }

    public void setCutscene(Cutscene cutscene) {
        this.currentscene = cutscene;
    }

    public void update(float partialTicks) {
        currentOffset = lerpVec3(currentOffset, targetOffset, lerpSpeed * partialTicks);
        currentRotation = lerpVec2(currentRotation, targetRotation, lerpSpeed * partialTicks);

    }

    public void setTargetOffset(FastVec3f offset) {
        this.targetOffset = offset;
    }

    public void setTargetRotation(FastVec2f rotation) {
        this.targetRotation = rotation;
    }

    public FastVec3f getCurrentOffset() {
        return currentOffset;
    }

    public FastVec2f getCurrentRotation() {
        return currentRotation;
    }

    private FastVec3f lerpVec3(FastVec3f start, FastVec3f end, float factor) {
        float x = Mth.lerp(factor, start.x(), end.x());
        float y = Mth.lerp(factor, start.y(), end.y());
        float z = Mth.lerp(factor, start.z(), end.z());
        return new FastVec3f(x, y, z);
    }

    private FastVec2f lerpVec2(FastVec2f start, FastVec2f end, float factor) {
        float x = lerpAngle(start.x(), end.x(), factor);
        float y = Mth.lerp(factor, start.y(), end.y());
        return new FastVec2f(x, y);
    }

    private float lerpAngle(float current, float target, float factor) {
        float difference = (target - current) % 360;
        if (difference > 180) difference -= 360;
        if (difference < -180) difference += 360;
        return current + difference * factor;
    }

    public void setSelected(LivingEntity entity) {
        this.selected = entity;
    }

    public LivingEntity getSelected() {
        return selected;
    }


    public FastVec2f computeAngles(float partialTicks) {
        update(partialTicks);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return this.angles;

        if (currentscene != null){
            setTargetOffset(currentscene.getCurrentKeyframe().position());
            if (focusPoint != null) {
                decoupled = false;
                Vec3 vec3 = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

                float targetYaw = (float) (Math.atan2(focusPoint.z() - vec3.z, focusPoint.x() - vec3.x) * Mth.RAD_TO_DEG - 90f);
                float dx = (float) (focusPoint.x() - vec3.x);
                float dz = (float) (focusPoint.z() - vec3.z);
                float horizontalDistance = (float) Math.sqrt(dx * dx + dz * dz);
                float dy = (float) (focusPoint.y() - (player.getY() + player.getEyeHeight()));
                float targetPitch = (float) -Math.atan2(dy, horizontalDistance) * Mth.RAD_TO_DEG;
                float lerpSpeed = 0.2f;
                float newYaw = lerpAngle(angles.x(), targetYaw, lerpSpeed);
                float newPitch = Mth.lerp(lerpSpeed, angles.y(), targetPitch);
                angles = new FastVec2f(newYaw, newPitch);
            }
            return this.angles;
        }

        if (selected == null) {
            setTargetOffset(new FastVec3f(-0.15f, 0.5f, -2f));
            decoupled = true;
            this.focusPoint = null;
            return this.angles;
        }

        this.focusPoint = FastVec3f.fromVec3(selected.position().add(0, selected.getBbHeight() / 1.5f, 0));
        if (focusPoint != null) {
            decoupled = false;
            setTargetOffset(new FastVec3f(-0.15f, 1, -2.5f));

            float targetYaw = (float) (Math.atan2(focusPoint.z() - player.getZ(), focusPoint.x() - player.getX()) * Mth.RAD_TO_DEG - 90f);

            float dx = (float) (focusPoint.x() - player.getX());
            float dz = (float) (focusPoint.z() - player.getZ());
            float horizontalDistance = (float) Math.sqrt(dx * dx + dz * dz);
            float dy = (float) (focusPoint.y() - (player.getY() + player.getEyeHeight()));
            float targetPitch = (float) -Math.atan2(dy, horizontalDistance) * Mth.RAD_TO_DEG;

            float lerpSpeed = 0.2f;
            float newYaw = lerpAngle(angles.x(), targetYaw, lerpSpeed);
            float newPitch = Mth.lerp(lerpSpeed, angles.y(), targetPitch);

            angles = new FastVec2f(newYaw, newPitch);
        }

        return angles;
    }




    public void handleMouseMovement(LocalPlayer player, double yRot, double xRot) {
        if (player != null) {
            player.setXRot(angles.y());
            float deltaYaw = (float) (yRot * 0.13f);
            float deltaPitch = (float) (xRot * 0.13f);
            if (selected == null) {
                angles = angles.add(deltaYaw, deltaPitch);


                angles = new FastVec2f(
                        angles.x() % 360,
                        Mth.clamp(angles.y(), -80.0f, 80.0f)
                );
                return;
            }

            FastVec2f tried = angles.add(deltaYaw, deltaPitch);
            tried = new FastVec2f(tried.x() % 360, tried.y());
            FastVec2f antiqued = tried.sub(angles);

            if (selected != null && focusPoint != null) {
                if (antiqued.length() >= 1) {
                    float pitchRadians = FastLibMathUtils.degreeToRadians(deltaPitch);
                    FastVec3f direction = new FastVec3f(1, 1, 1)
                            .rotate(deltaYaw + this.angles.x())
                            .add(0, (float) Math.sin(-(pitchRadians)), 0)
                            .normalize()
                            .scale((float) Math.sinh(-(pitchRadians)));
                    FastVec3f worldPos = direction.add(focusPoint);

                    float minDistance = Float.MAX_VALUE;
                    LivingEntity closestEntity = null;

                    for (Entity entity : player.level().getEntities(player, FastLibMathUtils.createAABBAroundPos(worldPos, 10))) {
                        if (entity instanceof Mob && entity != selected) {
                            float disinverted = focusPoint.distanceTo(FastVec3f.fromVec3(entity.position()));
                            float dist = worldPos.distanceTo(FastVec3f.fromVec3(entity.position()));
                            if (dist < disinverted && dist < minDistance && dist < entity.getBbHeight() * 10) {
                                minDistance = dist;
                                closestEntity = (LivingEntity) entity;
                            }
                        }
                    }

                    if (closestEntity != null) {
                        setSelected(closestEntity);
                    }
                }
            }
            player.setXRot(angles.y());
        }
    }

    private float currentSmoothedYaw = 0;

    public void handlePlayerMovement(Input input) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        if (decoupled) {
            Vec2 movement = input.getMoveVector();
            if (movement.lengthSquared() > 0) {

                float targetDeg = (float) Mth.atan2(movement.x, movement.y) * -Mth.RAD_TO_DEG;
                targetDeg += Minecraft.getInstance().gameRenderer.getMainCamera().getYRot();

                float partialTicks = Minecraft.getInstance().getFrameTime();
                float lerpFactor = 2.5f;
                currentSmoothedYaw = lerpAngle(currentSmoothedYaw, targetDeg, lerpFactor * partialTicks);

                player.setYRot(currentSmoothedYaw);

                input.up = false;
                input.down = false;
                input.right = false;
                input.left = false;
                input.forwardImpulse = 1;
                input.leftImpulse = 0;
            }
            else {
                prevRot = 0;
                currentSmoothedYaw = player.getYRot();
            }
        } else {

            float partialTicks = Minecraft.getInstance().getFrameTime();
            float lerpFactor = 2.5f;
            currentSmoothedYaw = lerpAngle(player.getYRot(), angles.x(), lerpFactor * partialTicks);
            player.setYRot(currentSmoothedYaw);
        }
    }
}