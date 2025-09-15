package com.robson.fastlib.api.camera;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.robson.fastlib.api.data.manager.PlayerDataManager;
import com.robson.fastlib.api.data.types.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import yesman.epicfight.client.gui.EntityUI;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class TargetMarker extends EntityUI {

    public static final TargetMarker Instance = new TargetMarker();

    public static final ResourceLocation STATUS_BAR = new ResourceLocation("fastlib", "textures/gui/marker.png");

    public boolean shouldDraw(LivingEntity entity, @Nullable LivingEntityPatch<?> entitypatch, LocalPlayerPatch playerpatch, float partialTicks) {
        PlayerData data = PlayerDataManager.get(playerpatch.getOriginal());
        if (data == null || data.getCamera().getTarget() == null) return false;
        return data.getCamera().getTarget().getId() == entity.getId();
    }

    public void draw(LivingEntity entity, @Nullable LivingEntityPatch<?> entitypatch, LocalPlayerPatch playerpatch, PoseStack poseStack, MultiBufferSource buffers, float partialTicks) {
        float scale = 0.5f +  0.25f * (entity.getBbHeight() / 1.8f);
       renderLockOnIndicator(poseStack, entity, scale);
    }


    private static long lastTime = 0;
    private static float rotationAngle = 0.0F;

    /**
     * Main rendering method with enhanced features - now only renders the indicator
     */
    public static void renderLockOnIndicator(PoseStack param, Entity target, float scale) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null || target == null) return;

        // Update animations
        updateAnimations();

        // Get the camera position
        Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().getPosition();

        // Get position of the entity with configurable offset
        Vec3 targetPos = target.position().add(0, target.getBbHeight() + 0.25f, 0);

        // Calculate relative position
        float x = (float) (targetPos.x - cameraPos.x);
        float y = (float) (targetPos.y - cameraPos.y);
        float z = (float) (targetPos.z - cameraPos.z);

        // Set up rendering
        param.pushPose();

        // Move to the target position
        param.translate(x, y, z);

        // Make the indicator always face the camera
        param.mulPose(minecraft.gameRenderer.getMainCamera().rotation());
        param.mulPose(new org.joml.Quaternionf().rotationY((float) Math.PI));

        // Set up render system
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Calculate dynamic size

        // Calculate dynamic color

        renderCustomIndicator(param, scale);

        // Clean up rendering
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();

        param.popPose();
    }

    /**
     * Renders custom image indicator with rotation and color tinting
     */
    private static void renderCustomIndicator(PoseStack poseStack, float size) {
        poseStack.pushPose();

        // Apply rotation animation
        poseStack.mulPose(new org.joml.Quaternionf().rotationZ(rotationAngle * (float) Math.PI / 180.0f));

        // Get the current custom indicator texture

        // Set up texture rendering
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, STATUS_BAR);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        Matrix4f matrix = poseStack.last().pose();

        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        // Define the quad vertices for the texture

        // Bottom-left
        bufferBuilder.vertex(matrix, -size, -size, 0.0F)
                .uv(0.0F, 1.0F)  // Bottom-left UV
                .color(255, 255,  255,  255)
                .endVertex();

        // Bottom-right
        bufferBuilder.vertex(matrix, size, -size, 0.0F)
                .uv(1.0F, 1.0F)  // Bottom-right UV
                .color(255, 255,  255,  255)
                .endVertex();

        // Top-right
        bufferBuilder.vertex(matrix, size, size, 0.0F)
                .uv(1.0F, 0.0F)  // Top-right UV
                .color(255, 255,  255,  255)
                .endVertex();

        // Top-left
        bufferBuilder.vertex(matrix, -size, size, 0.0F)
                .uv(0.0F, 0.0F)  // Top-left UV
                .color(255, 255,  255,  255)
                .endVertex();

        BufferUploader.drawWithShader(bufferBuilder.end());

        poseStack.popPose();
    }

    /**
     * Updates all animation effects
     */
    private static void updateAnimations() {
        long currentTime = System.currentTimeMillis();

        // First time initialization
        if (lastTime == 0) {
            lastTime = currentTime;
        }

        float deltaTime = (currentTime - lastTime) / 1000.0F;

        // Calculate rotation for animated indicators
        rotationAngle += deltaTime *  30.0F; // 30 degrees per second
        if (rotationAngle >= 360.0F) {
            rotationAngle -= 360.0F;
        }

        lastTime = currentTime;
    }
}