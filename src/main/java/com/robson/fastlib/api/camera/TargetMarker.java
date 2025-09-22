package com.robson.fastlib.api.camera;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.robson.fastlib.api.data.manager.PlayerDataManager;
import com.robson.fastlib.api.data.types.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
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

    private static long lastTime = 0;
    private static float rotationAngle = 0.0F;

    public boolean shouldDraw(LivingEntity entity, @Nullable LivingEntityPatch<?> entitypatch, LocalPlayerPatch playerpatch, float partialTicks) {
        PlayerData data = PlayerDataManager.get(playerpatch.getOriginal());
        if (data == null || data.getCamera().getTarget() == null) return false;
        return data.getCamera().getTarget().getId() == entity.getId();
    }

    public void draw(LivingEntity entity, @Nullable LivingEntityPatch<?> entitypatch, LocalPlayerPatch playerpatch, PoseStack poseStack, MultiBufferSource buffers, float partialTicks) {
        float scale = 0.5f + 0.25f * (entity.getBbHeight() / 1.8f);
        renderLockOnIndicator(poseStack, entity, scale, buffers);
    }

    public static void renderLockOnIndicator(PoseStack poseStack, Entity target, float scale, MultiBufferSource buffers) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null || target == null) return;

        updateAnimations();

        Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().getPosition();
        Vec3 targetPos = target.position().add(0, target.getBbHeight() + 0.25f, 0);

        float x = (float) (targetPos.x - cameraPos.x);
        float y = (float) (targetPos.y - cameraPos.y);
        float z = (float) (targetPos.z - cameraPos.z);

        poseStack.pushPose();

        // posiciona no mundo e alinha pra câmera
        poseStack.translate(x, y, z);
        poseStack.mulPose(minecraft.gameRenderer.getMainCamera().rotation());
        poseStack.mulPose(new org.joml.Quaternionf().rotationY((float) Math.PI));

        // aplica rotação local do sprite
        poseStack.pushPose();
        poseStack.mulPose(new org.joml.Quaternionf().rotationZ(rotationAngle * (float) Math.PI / 180.0f));

        // prevent depth writes (importantíssimo com shaders que fazem múltiplas passes)
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        // set shader + texture for buffer (RenderType will also set shader, mas isso garante)
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // --- background quad com RenderType custom ---
        VertexConsumer quad = buffers.getBuffer(LockOnRenderTypes.getLockOnQuads(STATUS_BAR));
        Matrix4f matrix = poseStack.last().pose();
        // cores em floats (r,g,b,a)
        float r = 1f, g = 1f, b = 1f, a = 1f;

        quad.vertex(matrix, -scale, -scale, 0.0F).uv(0.0F, 1.0F).color(r, g, b, a).endVertex();
        quad.vertex(matrix,  scale, -scale, 0.0F).uv(1.0F, 1.0F).color(r, g, b, a).endVertex();
        quad.vertex(matrix,  scale,  scale, 0.0F).uv(1.0F, 0.0F).color(r, g, b, a).endVertex();
        quad.vertex(matrix, -scale,  scale, 0.0F).uv(0.0F, 0.0F).color(r, g, b, a).endVertex();


        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();

        poseStack.popPose(); // pop rotação local
        poseStack.popPose(); // pop translate/align
    }

    private static void updateAnimations() {
        long currentTime = System.currentTimeMillis();
        if (lastTime == 0) lastTime = currentTime;
        float delta = (currentTime - lastTime) / 1000.0F;
        rotationAngle += delta * 30.0F;
        if (rotationAngle >= 360.0F) rotationAngle -= 360.0F;
        lastTime = currentTime;
    }

    public static class LockOnRenderTypes extends RenderType {

        protected LockOnRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize,
                                    boolean affectsCrumbling, boolean sortOnUpload,
                                    Runnable setupState, Runnable clearState) {
            super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
        }

        public static RenderType getLockOnQuads(ResourceLocation texture) {
            CompositeState composite = CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                    .setTransparencyState(TransparencyStateShard.NO_TRANSPARENCY)     // blending apropriado
                    .setDepthTestState(DepthTestStateShard.NO_DEPTH_TEST)         // <<< sem teste de profundidade
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionTexColorShader))
                    .setLightmapState(LightmapStateShard.LIGHTMAP)
                    .setOverlayState(OverlayStateShard.OVERLAY)
                    .setCullState(CullStateShard.NO_CULL)                    // sem culling
                    .createCompositeState(true);

            return RenderType.create("fastlib:lock_on_quads",
                    DefaultVertexFormat.POSITION_TEX_COLOR,
                    VertexFormat.Mode.QUADS,
                    256,
                    true,
                    false,
                    composite);
        }
    }
}