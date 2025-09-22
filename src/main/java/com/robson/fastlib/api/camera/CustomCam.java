package com.robson.fastlib.api.camera;


import com.robson.fastlib.api.utils.Scheduler;
import com.robson.fastlib.api.utils.math.FastLibMathUtils;
import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.SimplexNoise;
import org.joml.Vector3d;

import javax.xml.crypto.dsig.Transform;
import java.util.concurrent.TimeUnit;

public class CustomCam {

    private static final float smoothFactor = 0.05f;

    private boolean decoupled = true;

    private LivingEntity target;

    private final SmoothFloat smoothYaw;
    private final SmoothFloat smoothPitch;
    private final SmoothFloat smoothRoll;
    private final SmoothVec3 smoothPosition;
    boolean targetcooldown = false;
    private final Minecraft mc ;

    private Cutscene activeCutscene;
    private int cutsceneFrameIndex = 0;

    public void setDecoupled(boolean decoupled) {
        if (this.decoupled && (!decoupled || this.target != null)){
            this.decoupled = false;
            this.handleRotation(0, 0);
            return;
        }
        this.decoupled = decoupled;
    }

    public Minecraft getMc() {
        return mc;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public CustomCam() {
        this.smoothYaw = new SmoothFloat(0, smoothFactor);
        this.smoothPitch = new SmoothFloat(0, smoothFactor);
        this.smoothRoll = new SmoothFloat(0, smoothFactor);
        this.smoothPosition = new SmoothVec3(new FastVec3f(0, 0,0 ), smoothFactor);
        this.mc = Minecraft.getInstance();
    }

    public boolean isDecoupled() {
        return isCutsceneActive() || mc.player.getVehicle() != null || (decoupled && !mc.options.getCameraType().isFirstPerson() && target == null);
    }

    public boolean isEnabled() {
        return true;
    }

    public void handleRotation(float yaw, float pitch) {
        if (isCutsceneActive()){
            if (mc.player != null) {
                mc.player.setYRot(mc.player.getYRot() + yaw * 0.13f);
                mc.player.setXRot(Mth.clamp(mc.player.getXRot() + pitch * 0.13f, -70, 70));
            }
            return;
        }
        smoothYaw.setCurrent(smoothYaw.getCurrent() + yaw * 0.13f);
        smoothPitch.setCurrent(Mth.clamp(smoothPitch.getCurrent() + pitch * 0.13f, -70, 70));
        if (mc.player != null) {
            mc.player.setXRot(this.target != null ? smoothPitch.getCurrent() - 10 : smoothPitch.getCurrent());
            if (!isDecoupled()) mc.player.setYRot(smoothYaw.getCurrent());
        }
    }

    private float cutsceneTimer = 0f;
    private float cutsceneInterval = 0f;

    public void startCutscene(Cutscene cutscene) {
        startCutscene(cutscene, false);
    }

    public void startCutscene(Cutscene cutscene, boolean force) {
        if (cutscene != null && !cutscene.getKeyFrames().isEmpty() && (force || !isCutsceneActive())) {
            this.activeCutscene = cutscene;
            this.cutsceneFrameIndex = 0;
            this.cutsceneInterval = cutscene.getDuration() / cutscene.getKeyFrames().size();
            this.cutsceneTimer = 0f;
        }
    }


    public boolean isCutsceneActive() {
        return activeCutscene != null;
    }


    public void setPos(FastVec3f pos){
        this.smoothPosition.setTarget(pos);
    }

    public FastVec2f getRotation() {
        return new FastVec2f(smoothYaw.getCurrent(), smoothPitch.getCurrent());
    }

    public float getRoll(){
        return smoothRoll.getCurrent();
    }

    public FastVec3f getOffset() {
        return smoothPosition.getCurrent();
    }

    /**
     * Chamado a cada frame/tick com deltaTime em segundos.
     */
    public void update(float deltaTime) {
        if (mc.player == null) return;
        if (isCutsceneActive()) {
            cutsceneTimer += deltaTime;
            while (cutsceneTimer >= cutsceneInterval && cutsceneFrameIndex < activeCutscene.getKeyFrames().size()) {
                Cutscene.CutsceneKeyFrame keyFrame = activeCutscene.getKeyFrames().get(cutsceneFrameIndex);
                smoothPosition.setTarget(keyFrame.getPosition());
                smoothYaw.setTarget(keyFrame.getRotation().x());
                smoothPitch.setTarget(keyFrame.getRotation().y());
                cutsceneFrameIndex++;
                cutsceneTimer -= cutsceneInterval;
            }
            if (cutsceneFrameIndex >= activeCutscene.getKeyFrames().size()) {
                activeCutscene = null;
            }
        }
        if (this.target != null) {
            if ( !this.target.isAlive() || this.target.isRemoved() || (this.mc.player.getVehicle() != null && this.mc.player.getVehicle().getUUID().equals(this.target.getUUID()))){
                setTarget(null);
            }
            else {
                FastVec3f playerPos = FastVec3f.fromVec3(mc.player.position());
                FastVec3f targetPos = FastVec3f.fromVec3(this.target.position());

                // Vetor horizontal
                FastVec2f xz = new FastVec2f(targetPos.x() - playerPos.x(), targetPos.z() - playerPos.z());
                // Vetor vertical
                float horizontalDist = xz.length();
                float dy = targetPos.y() - playerPos.y();

                float yaw = (float) (Math.toDegrees(Math.atan2(xz.y(), xz.x()))) - 90;

                float pitch = -(float) (Math.toDegrees(Math.atan2(dy, horizontalDist)));

                smoothYaw.setTarget(FastLibMathUtils.wrapDegrees(yaw));
                smoothPitch.setTarget(FastLibMathUtils.wrapDegrees(pitch + 20 - (target.getBbHeight() * 4)));
            }
        }

        smoothYaw.update(deltaTime);
        smoothPitch.update(deltaTime);
        smoothRoll.update(deltaTime);
        smoothPosition.update(deltaTime);

    }

   public void setRoll(float roll) {
        this.smoothRoll.setTarget(roll);
   }
    public void selectNearestTarget(LivingEntity referenceEntity, Vec3 referencePosition, float searchradius, boolean targetChange){
        if (searchradius > 50){
            searchradius = 50;
        }
        if (referenceEntity != null && referencePosition != null && mc.player != null){
            LivingEntity selected = null;
            float intial = Float.MAX_VALUE;
            for (Entity entity : referenceEntity.level().getEntities(referenceEntity, FastLibMathUtils.createAABBAroundEnt(referenceEntity, searchradius))){
                if(entity instanceof LivingEntity
                        && !entity.getUUID().equals(referenceEntity.getUUID())
                        && !entity.getUUID().equals(mc.player.getUUID())
                        && (mc.player.getVehicle() == null || !entity.getUUID().equals(mc.player.getVehicle().getUUID()))){
                    float dist = (float) referencePosition.distanceTo(entity.position());
                    if (dist < intial) {
                        if (targetChange && dist > referencePosition.distanceTo(referenceEntity.position())){
                            continue;
                        }
                        selected = (LivingEntity) entity;
                        intial = dist;
                    }
                }
            }
            if (selected != null) {
                targetcooldown = true;
                this.setTarget(selected);
                Scheduler.schedule(() -> targetcooldown = false, 500, TimeUnit.MILLISECONDS);
            }
        }

    }

    public void triggerTargetChange(float dx, float dy) {
        FastVec2f movedVector = new FastVec2f(dx, dy);
        float length = movedVector.length();
        if (!targetcooldown && length > 7 &&  mc.player != null) {
            movedVector = movedVector.rotate(mc.player.getYRot()).scale(0.75f);
            Vec3 targetvec = new Vec3(
                    (float) (target.getX() - movedVector.x()),
                    (float) (target.getY() + target.getBbHeight() / 2 - (dy / 3)),
                    (float) (target.getZ() - movedVector.y()));
            selectNearestTarget( target, targetvec, length * 4, true);
        }
    }




    public void handlePlayerMovement(Input input, short angle) {
        if (mc.player == null || !isDecoupled()) return;

        // 1. Pega o yaw da câmera
        float cameraYaw = mc
                .gameRenderer
                .getMainCamera()
                .getYRot();


        // 3. Angulo desejado em world-space
        float desiredYaw = Mth.wrapDegrees(cameraYaw + angle);

        // 4. Diferença angular normalizada [-180,180)
        float currentYaw = mc.player.getYRot();
        float diff = Mth.wrapDegrees(desiredYaw - currentYaw);

        // 5. Se for lateral/frente, aplica clamp a ±90°
        if (Math.abs(diff) < 135f) {
            diff = Mth.clamp(diff, -90f, 90f);
        }

        // 6. Interpola pelo menor caminho
        float smoothing = 0.5f;
        float newYaw = currentYaw + diff * smoothing;
        mc.player.setYRot(Mth.wrapDegrees(newYaw));

        // 7. Zera inputs e força andar pra frente
        input.up = input.down = input.left = input.right = false;
        input.forwardImpulse = 1;
        input.leftImpulse = 0;
    }

}