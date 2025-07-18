package com.robson.fastlib.api.camera;


import com.robson.fastlib.api.utils.Scheduler;
import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CustomCam {

    protected float prevRot = 0;

    private FastVec3f currentOffset = FastVec3f.ZERO;
    private FastVec2f currentRotation = FastVec2f.ZERO;

    private FastVec3f targetOffset = FastVec3f.ZERO;
    private FastVec2f targetRotation = FastVec2f.ZERO;

    // Velocidade de interpolação (ajuste conforme necessário)
    private final float lerpSpeed = 0.2f;

    private FastVec3f focusPoint;

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


    public FastVec2f computeAngles(float partialTicks) {
        update(partialTicks);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return this.angles;

        Entity selected = null;
        for (Entity entity : player.level().getEntities(player, player.getBoundingBox().inflate(10))) {
            if (entity instanceof Mob) {
                selected = entity;
                break;
            }
        }

        if (selected == null) {
            setTargetOffset(new FastVec3f(-0.15f, 0.5f, -4f));
            decoupled = true;
            this.focusPoint = null;
            return this.angles;
        }

        this.focusPoint = FastVec3f.fromVec3(selected.position().add(0, selected.getBbHeight() / 1.5f, 0));
        if (focusPoint != null) {
            decoupled = false;
            setTargetOffset(new FastVec3f(-0.15f, 1, -4.5f));

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
        float deltaYaw = (float) (yRot * 0.13f);
        float deltaPitch = (float) (xRot * 0.13f);

        angles = angles.add(deltaYaw, deltaPitch);

        angles = new FastVec2f(
                angles.x() % 360,
                Mth.clamp(angles.y(), -80.0f, 80.0f)
        );

        if (player != null) {
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
            } else {
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