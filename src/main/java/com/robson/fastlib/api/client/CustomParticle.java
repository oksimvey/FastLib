package com.robson.fastlib.api.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.particle.EpicFightParticles;

@OnlyIn(Dist.CLIENT)
public class CustomParticle extends TextureSheetParticle {

    static final ParticleRenderType BRIGHT = new ParticleRenderType() {
        public void begin(BufferBuilder p_107448_, TextureManager p_107449_) {
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            p_107448_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        public void end(Tesselator p_107451_) {
            p_107451_.end();
        }

        public String toString() {
            return "BRIGHT";
        }
    };

    public enum MotionType {
        CIRCLE,
        FIXED,
        RANDOM
    }


    private final SpriteSet sprites;

    public CustomParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.friction = .77f;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize *= 1f;
        this.scale(1.5f);
        this.lifetime = 5 + (int) (Math.random() * 15);
        sprites = spriteSet;
        this.gravity = 0.0F;
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        randomFactor = ThreadLocalRandom.current().nextFloat() - 0.5f;

    }

    private float randomFactor;

    @Override
    public void tick() {
        super.tick();
        this.xd = Math.cos(Math.toRadians(this.lifetime * 10)) * randomFactor/ 5;
        this.zd = Math.sin(Math.toRadians(this.lifetime * 10)) * randomFactor/ 5;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return BRIGHT;
    }

    @Override
    protected int getLightColor(float p_107249_) {
        return LightTexture.FULL_BRIGHT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new CustomParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
