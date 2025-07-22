package com.robson.fastlib.api.camera;


import com.robson.fastlib.api.utils.math.BezierCurve;
import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.SwordItem;

import java.util.ArrayList;
import java.util.List;

public class CustomCam {

    private static final float smoothFactor = 0.05f;

    private final SmoothFloat smoothYaw;
    private final SmoothFloat smoothPitch;
    private final SmoothVec3 smoothPosition;
    private List<FastVec3f> pathCurve;
    private int curveindex = 0;
    private float elapsed;
    private float duration;

    public CustomCam(float initYaw, float initPitch, FastVec3f initPos) {
        this.smoothYaw = new SmoothFloat(initYaw, smoothFactor);
        this.smoothPitch = new SmoothFloat(initPitch, smoothFactor);
        this.smoothPosition = new SmoothVec3(initPos, smoothFactor);
        this.pathCurve = new ArrayList<>();
        this.elapsed = 0f;
        this.duration = 1f;
    }

    public boolean isEnabled() {
        return smoothPosition != null;
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
        if (Minecraft.getInstance().player.getMainHandItem().getItem() instanceof SwordItem){
            if (pathCurve.isEmpty()) {
                setPathCurve(
                        BezierCurve.getBezierInterpolatedPoints(List.of(
                                new FastVec3f(0, 0.5f, 2),
                                new FastVec3f(2, 0.5f, 0),
                                new FastVec3f(0, 0.5f, -2),
                                new FastVec3f(-2, 0.5f, 0),
                                new FastVec3f(0, 0.5f, 2)), 10)
                        , 10);
            }
        }



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
        else setPos(new FastVec3f(-0.25f, 0.5f, -0.75f));

        smoothYaw.update(deltaTime);
        smoothPitch.update(deltaTime);
        smoothPosition.update(deltaTime);

    }
}