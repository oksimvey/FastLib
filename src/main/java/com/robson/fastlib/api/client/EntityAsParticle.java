package com.robson.fastlib.api.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.utils.EntitySnapshot;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.client.particle.CustomModelParticle;
import yesman.epicfight.client.particle.EpicFightParticleRenderTypes;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class EntityAsParticle extends CustomModelParticle<SkinnedMesh> {

    protected final EntitySnapshot<?> entitySnapshot;

    protected final Consumer<EntityAsParticle> ticktask;

    public EntityAsParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, EntitySnapshot<?> entitySnapshot, Consumer<EntityAsParticle> ticktask) {
        super(level, x, y, z, xd, yd, zd, (AssetAccessor)null);
        this.entitySnapshot = entitySnapshot;
        this.ticktask = ticktask;
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.alpha = 1.0F;
        this.yawO = entitySnapshot.getYRot();
        this.yaw = entitySnapshot.getYRot();
    }

    public void tick() {
        super.tick();
        this.ticktask.accept(this);
    }

    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        int lightColor = this.getLightColor(partialTicks);
        PoseStack poseStack = new PoseStack();
        this.setupPoseStack(poseStack, camera, partialTicks);
        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        this.entitySnapshot.renderTextured(poseStack, buffers, EpicFightRenderTypes::entityAfterimageStencil, Mesh.DrawingFunction.POSITION_TEX, 0, 0.0F, 0.0F, 0.0F, 1.0F);
        this.entitySnapshot.renderItems(poseStack, buffers, EpicFightRenderTypes.itemAfterimageStencil(), Mesh.DrawingFunction.POSITION_TEX, lightColor, 1.0F);
        buffers.endLastBatch();
        this.entitySnapshot.renderTextured(poseStack, buffers, EpicFightRenderTypes::entityAfterimageTranslucent, Mesh.DrawingFunction.NEW_ENTITY, lightColor, this.rCol, this.gCol, this.bCol, alpha);
        this.entitySnapshot.renderItems(poseStack, buffers, EpicFightRenderTypes.itemAfterimageTranslucent(), Mesh.DrawingFunction.NEW_ENTITY, lightColor, alpha);
        buffers.endLastBatch();
        this.revert(poseStack);
    }

    public ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.ENTITY_PARTICLE;
    }

    protected void setupPoseStack(PoseStack poseStack, Camera camera, float partialTick) {
        poseStack.pushPose();
        poseStack.mulPoseMatrix(RenderSystem.getModelViewStack().last().pose());
        RenderSystem.getModelViewStack().pushPose();
        RenderSystem.getModelViewStack().setIdentity();
        RenderSystem.applyModelViewMatrix();
        Vec3 cameraPosition = camera.getPosition();
        float x = (float)(Mth.lerp((double)partialTick, this.xo, this.x) - cameraPosition.x());
        float y = (float)(Mth.lerp((double)partialTick, this.yo, this.y) - cameraPosition.y());
        float z = (float)(Mth.lerp((double)partialTick, this.zo, this.z) - cameraPosition.z());
        poseStack.translate(x, y, z);
        Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
        rotation.mul(QuaternionUtils.YP.rotationDegrees(180.0F));
        poseStack.mulPose(rotation);
        poseStack.mulPoseMatrix(OpenMatrix4f.exportToMojangMatrix(this.entitySnapshot.getModelMatrix()));
        float scale = Mth.lerp(partialTick, this.scaleO, this.scale);
        poseStack.translate(0.0F, this.entitySnapshot.getHeightHalf(), 0.0F);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0F, -this.entitySnapshot.getHeightHalf(), 0.0F);
    }

    protected void revert(PoseStack poseStack) {
        poseStack.popPose();
        RenderSystem.getModelViewStack().popPose();
        RenderSystem.applyModelViewMatrix();
    }



    @OnlyIn(Dist.CLIENT)
    public static class WhiteAfterimageProvider implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Entity entity = level.getEntity((int)Double.doubleToLongBits(xSpeed));
            LivingEntityPatch<?> entitypatch = (LivingEntityPatch)EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
            if (entitypatch != null) {
                EntitySnapshot<?> entitySnapshot = entitypatch.captureEntitySnapshot();
                if (entitySnapshot != null) {
                    EntityAsParticle afterimage = new EntityAsParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, entitySnapshot, (particle) ->
                            particle.alpha = (float)1);
                    afterimage.setLifetime((int) ySpeed);
                    return afterimage;
                }
            }

            return null;
        }
    }
}