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
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CustomCam {

    private static final float smoothFactor = 0.05f;

    private boolean decoupled = true;

    private LivingEntity target;

    private final SmoothFloat smoothYaw;
    private final SmoothFloat smoothPitch;
    private final SmoothVec3 smoothPosition;
    private final PlayerPatch<Player> playerPatch;
    boolean targetcooldown = false;

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

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public CustomCam(float initYaw, float initPitch, FastVec3f initPos, PlayerPatch<Player> playerPatch) {
        this.playerPatch = playerPatch;
        this.smoothYaw = new SmoothFloat(initYaw, smoothFactor);
        this.smoothPitch = new SmoothFloat(initPitch, smoothFactor);
        this.smoothPosition = new SmoothVec3(initPos, 0.025f);
    }

    public boolean isDecoupled() {
        return decoupled && !Minecraft.getInstance().options.getCameraType().isFirstPerson() && target == null;
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

    public void startCutscene(Cutscene cutscene, boolean force) {
        if (cutscene != null && !cutscene.getKeyFrames().isEmpty() && (force || !isCutsceneActive())) {
            this.activeCutscene = cutscene;
            this.cutsceneFrameIndex = 0;
            float interval = cutscene.getDuration() / cutscene.getKeyFrames().size() * 1000;
            playCutscene((long) interval, this.playerPatch != null ? this.playerPatch.getYRot() : 0f);
        }
    }

    private void playCutscene(long interval, float intialrot){
        if (this.activeCutscene.getKeyFrames().size() > cutsceneFrameIndex){
            Cutscene.CutsceneKeyFrame keyFrame = this.activeCutscene.getKeyFrames().get(cutsceneFrameIndex);
            if (this.activeCutscene.getType() == Cutscene.Type.LOCAL && this.playerPatch != null){
                FastVec3f pos = keyFrame.getPosition().rotate(this.playerPatch.getYRot());
                this.smoothPosition.setTarget(pos);
                this.smoothYaw.setTarget(keyFrame.getRotation().x() + intialrot);
                this.smoothPitch.setTarget(keyFrame.getRotation().y());
            }
            else {
                smoothPosition.setTarget(keyFrame.getPosition());
                smoothYaw.setTarget(keyFrame.getRotation().x());
                smoothPitch.setTarget(keyFrame.getRotation().y());
            }
            cutsceneFrameIndex++;
            Scheduler.schedule(()-> {
                playCutscene(interval, intialrot);
            }, interval, TimeUnit.MILLISECONDS);
        }
        else this.activeCutscene = null;
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

    public FastVec3f getOffset() {
        return smoothPosition.getCurrent();
    }

    /**
     * Chamado a cada frame/tick com deltaTime em segundos.
     */
    public void update(float deltaTime) {
        if (Minecraft.getInstance().player == null) return;


        if (this.target == null || !this.target.isAlive() || this.target.isRemoved()) {
            setTarget(null);
        }

        if (this.target != null) {
            FastVec3f playerPos = FastVec3f.fromVec3(Minecraft.getInstance().player.position());
            FastVec3f targetPos = FastVec3f.fromVec3(this.target.position());

            // Vetor horizontal
            FastVec2f xz = new FastVec2f(targetPos.x() - playerPos.x(), targetPos.z() - playerPos.z());
            // Vetor vertical
            float horizontalDist = xz.length();
            float dy = targetPos.y() - playerPos.y();

            float yaw = (float) (Math.toDegrees(Math.atan2(xz.y(), xz.x())))  -  90;

            float pitch = -(float)( Math.toDegrees(Math.atan2(dy, horizontalDist))) ;

            smoothYaw.setTarget(FastLibMathUtils.wrapDegrees(yaw));
            smoothPitch.setTarget(FastLibMathUtils.wrapDegrees(pitch));

        }

        smoothYaw.update(deltaTime);
        smoothPitch.update(deltaTime);
        smoothPosition.update(deltaTime);

    }

    public void selectNearestTarget(LivingEntity referenceEntity, Vec3 referencePosition, float searchradius, boolean targetChange){
        if (searchradius > 50){
            searchradius = 50;
        }
        if (referenceEntity != null && referencePosition != null){
            LivingEntity selected = null;
            float intial = Float.MAX_VALUE;
            for (Entity entity : referenceEntity.level().getEntities(referenceEntity, FastLibMathUtils.createAABBAroundEnt(referenceEntity, searchradius))){
                if (entity instanceof LivingEntity && entity != playerPatch.getOriginal() && entity != referenceEntity){
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
        if (!targetcooldown && length > 7) {
            movedVector = movedVector.rotate(playerPatch.getYRot()).scale(0.75f);
            Vec3 targetvec = new Vec3(
                    (float) (target.getX() - movedVector.x()),
                    (float) (target.getY() + target.getBbHeight() / 2 - (dy / 3)),
                    (float) (target.getZ() - movedVector.y()));
            selectNearestTarget(target, targetvec, length * 4, true);
        }
    }




    public void handlePlayerMovement(Input input, short angle) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || !isDecoupled()) return;

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