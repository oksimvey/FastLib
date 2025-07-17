package com.robson.fastlib.api.camera;


import com.robson.fastlib.api.utils.math.FastLibMathUtils;
import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class CustomCam {

    protected float prevRot = 0;

    protected FastVec3f offset = new FastVec3f(0, 0, 0);

    private FastVec3f focusPoint;

    protected FastVec2f angles = new FastVec2f(0, 0);

    private volatile boolean decoupled = false;

    public boolean disableRearCamera() {
        return true;
    }

    public void changeOffset(FastVec3f offset) {
        this.offset = offset;
    }

    public FastVec3f getOffset() {
        return offset;
    }

    public FastVec2f computeAngles() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return this.angles;
        if (focusPoint != null) {
            float angle = (float) (Math.atan2(focusPoint.x() - player.getX(), focusPoint.z() - player.getZ()) * -Mth.RAD_TO_DEG);
            return new FastVec2f(angle, angles.y());
        }
        Entity selected = null;
        for (Entity entity : player.level().getEntities(player, player.getBoundingBox().inflate(10))) {
            if (entity != null){
                selected = entity;
                break;
            }
        }
        if (selected == null) return this.angles;
        this.focusPoint = FastVec3f.fromVec3(selected.position());
        return angles;
    }

    public void handleMouseMovement(LocalPlayer player, double yRot, double xRot) {
        angles = angles.add(
                (float) (yRot * 0.13),
                (float) (xRot * 0.13)
        );
        if (player == null) return;
        player.setXRot(angles.y());
    }

    public void handlePlayerMovement(Input input) {
        LocalPlayer player = Minecraft.getInstance().player;
        Vec2 movement = input.getMoveVector();
        if (movement.lengthSquared() > 0) {
            float deg = (float) Mth.atan2(movement.x, movement.y) * -Mth.RAD_TO_DEG;
                deg += Minecraft.getInstance().gameRenderer.getMainCamera().getYRot();
            player.setYRot(prevRot + deg);
            input.up = false;
            input.down = false;
            input.right = false;
            input.left = false;
            input.forwardImpulse = 1;
            input.leftImpulse = 0;
        }
        else {
            prevRot =  0;
        }
    }
}