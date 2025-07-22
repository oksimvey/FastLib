package com.robson.fastlib.mixins;


import com.robson.fastlib.api.camera.CustomCam;
import com.robson.fastlib.api.data.manager.PlayerDataManager;
import com.robson.fastlib.api.data.types.PlayerData;
import com.robson.fastlib.api.utils.math.FastLibMathUtils;
import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraInvoker {

    @Shadow private boolean initialized;

    @Shadow private BlockGetter level;

    @Shadow private Entity entity;

    @Shadow private boolean detached;

    @Shadow protected abstract void setRotation(float p_90573_, float p_90574_);

    @Shadow protected abstract void setPosition(Vec3 p_90582_);

    @Shadow private float yRot;

    @Shadow private float xRot;

    @Shadow protected abstract void move(double p_90569_, double p_90570_, double p_90571_);

    @Shadow protected abstract double getMaxZoom(double p_90567_);

    @Shadow private float eyeHeightOld;

    @Shadow private float eyeHeight;

    @Inject(method = "setup", at = @At(value = "HEAD"), cancellable = true)
    public void setup(BlockGetter p_90576_, Entity p_90577_, boolean p_90578_, boolean p_90579_, float p_90580_, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && PlayerDataManager.get(player) != null) {
            CustomCam cam = PlayerDataManager.get(player).getCamera();
            if (cam != null) {
                ci.cancel();
                cam.update(p_90580_);
                this.initialized = true;
                this.level = p_90576_;
                this.entity = p_90577_;
                this.detached = p_90578_;
                FastVec2f angles = cam.getRotation();
                setRotation(angles.x(), angles.y());
                FastVec3f offset = Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON ? new FastVec3f(0, 0, 0) :
                        cam.getOffset().rotate(this.yRot);
                float pitchRadians = FastLibMathUtils.degreeToRadians(this.xRot);
                float verticalOffset = (float) Math.sin(pitchRadians);
                this.setPosition(new Vec3(
                        Mth.lerp(p_90580_, p_90577_.xo, p_90577_.getX()),
                        Mth.lerp(p_90580_, p_90577_.yo, p_90577_.getY()) +
                                Mth.lerp(p_90580_, this.eyeHeightOld, this.eyeHeight) +
                                verticalOffset,
                        Mth.lerp(p_90580_, p_90577_.zo, p_90577_.getZ())
                ).add(offset.toVec3()));
                if (p_90578_) {
                    if (p_90579_) {
                        this.setRotation(this.yRot + 180.0F, -this.xRot);
                    }
                    this.move(-this.getMaxZoom(4.0D), 0.0D, 0.0D);
                } else if (p_90577_ instanceof LivingEntity && ((LivingEntity) p_90577_).isSleeping()) {
                    Direction direction = ((LivingEntity) p_90577_).getBedOrientation();
                    this.setRotation(direction != null ? direction.toYRot() - 180.0F : 0.0F, 0.0F);
                    this.move(0.0D, 0.3D, 0.0D);
                }
            }
        }
    }

}
