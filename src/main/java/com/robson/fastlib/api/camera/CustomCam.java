package com.robson.fastlib.api.camera;


import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;

public class CustomCam {

    private static final float smoothFactor = 0.05f;

    private boolean decoupled = true;

    private LivingEntity target;

    private final SmoothFloat smoothYaw;
    private final SmoothFloat smoothPitch;
    private final SmoothVec3 smoothPosition;
    private List<FastVec3f> pathCurve;
    private int curveindex = 0;
    private float elapsed;
    private float duration;

    public void setDecoupled(boolean decoupled) {
        if (this.decoupled && !decoupled){
            this.decoupled = false;
            this.handleRotation(0, 0);
        }
        this.decoupled = decoupled;
    }

    public CustomCam(float initYaw, float initPitch, FastVec3f initPos) {
        this.smoothYaw = new SmoothFloat(initYaw, smoothFactor);
        this.smoothPitch = new SmoothFloat(initPitch, smoothFactor);
        this.smoothPosition = new SmoothVec3(initPos, smoothFactor);
        this.pathCurve = new ArrayList<>();
    }

    public boolean isDecoupled() {
        return decoupled ? !Minecraft.getInstance().options.getCameraType().isFirstPerson() : decoupled;
    }

    public boolean isEnabled() {
        return true;
    }

    public void handleRotation(float yaw, float pitch) {
        smoothYaw.setCurrent(smoothYaw.getCurrent() + yaw * 0.13f);
        smoothPitch.setCurrent(Mth.clamp(smoothPitch.getCurrent() + pitch * 0.13f, -70, 70));
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.setXRot(smoothPitch.getCurrent());
            if (!isDecoupled()) Minecraft.getInstance().player.setYRot(smoothYaw.getCurrent());
        }
    }


    /**
     * Define um caminho de câmera por Bézier 3D.
     * duration em segundos.
     */
    public void setPathCurve(List<FastVec3f> curve, float duration) {
        this.pathCurve = curve;
        this.duration = duration;
        this.elapsed = 0f;
    }


    public void setPos(FastVec3f pos){
        this.smoothPosition.setTarget(pos);
    }

    public FastVec2f getRotation() {
        return new FastVec2f(smoothYaw.getCurrent(), smoothPitch.getCurrent());
    }

    public FastVec3f getOffset() {
        return smoothPosition.getCurrent();
    }

    /**
     * Chamado a cada frame/tick com deltaTime em segundos.
     */
    public void update(float deltaTime) {
        if (Minecraft.getInstance().player == null) return;

        if (pathCurve != null) {
            if (curveindex >= pathCurve.size()){
                curveindex = 0;
                pathCurve = new ArrayList<>();

            }
            else {
                FastVec3f target = pathCurve.get(curveindex);
                smoothPosition.setTarget(target);
                if (this.smoothPosition.getCurrent().distanceTo(target) < 0.5f) {
                    curveindex++;
                }
            }
        }

        smoothYaw.update(deltaTime);
        smoothPitch.update(deltaTime);
        smoothPosition.update(deltaTime);

    }

    public void handlePlayerMovement(Input input, short angle) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || !decoupled) return;

        // 1. Pega o yaw da câmera
        float cameraYaw = Minecraft.getInstance()
                .gameRenderer
                .getMainCamera()
                .getYRot();


        // 3. Angulo desejado em world-space
        float desiredYaw = Mth.wrapDegrees(cameraYaw + angle);

        // 4. Diferença angular normalizada [-180,180)
        float currentYaw = player.getYRot();
        float diff = Mth.wrapDegrees(desiredYaw - currentYaw);

        // 5. Se for lateral/frente, aplica clamp a ±90°
        if (Math.abs(diff) < 135f) {
            diff = Mth.clamp(diff, -90f, 90f);
        }

        // 6. Interpola pelo menor caminho
        float smoothing = 0.5f;
        float newYaw = currentYaw + diff * smoothing;
        player.setYRot(Mth.wrapDegrees(newYaw));

        // 7. Zera inputs e força andar pra frente
        input.up = input.down = input.left = input.right = false;
        input.forwardImpulse = 1;
        input.leftImpulse = 0;
    }

}