package com.robson.fastlib.api.utils;

import com.robson.fastlib.api.utils.math.FastLibMathUtils;
import com.robson.fastlib.api.utils.math.FastVec3f;
import com.robson.fastlib.events.ParticleRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.particle.EpicFightParticles;

import java.util.ArrayList;
import java.util.List;

public interface VfxUtils {

    static void spawnAfterImageParticle(LivingEntity entity) {
        if (entity != null) {
            Minecraft.getInstance().particleEngine.createParticle(EpicFightParticles.WHITE_AFTERIMAGE.get(),
                    entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);

        }
    }

    static void spawnBlockBreakParticle(LivingEntity entity, float radius) {
        if (entity == null || entity.level().isClientSide) return;
        LevelUtil.circleSlamFracture(entity, entity.level(), entity.position().subtract(0, 0.1, 0), radius);
    }

    static void spawnEntityParticle(LivingEntity entity, int lifetime) {
        if (entity != null) {
            Minecraft.getInstance().particleEngine.createParticle(ParticleRegister.ENTITY.get(),
                    entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), lifetime, 0);

        }
    }

    static void drawSphere(FastVec3f center, float radius, ParticleOptions particleOptions, int precision) {
        for (int j = 0; j < 360; j += 90 / precision) {
            List<FastVec3f> points = new ArrayList<>();
            float y = (float) (radius * Math.cos(FastLibMathUtils.degreeToRadians(j)));
            for (int i = 0; i <= 360; i += 90 / precision) {
                float degree = FastLibMathUtils.degreeToRadians(i);
                points.add(center.add((float) (y * Math.cos(degree)), (float) (radius * Math.sin(FastLibMathUtils.degreeToRadians(j))), (float) (y * Math.sin(degree))));
            }
            for (FastVec3f point : points) {
                Minecraft.getInstance().particleEngine.createParticle(
                        particleOptions,
                        point.x(), point.y(), point.z(),
                        0, 0, 0
                );
            }
        }
    }

    static void drawTorus(FastVec3f center, float centerRadius, float radius, ParticleOptions particleOptions, int precision) {
        for (int j = 0; j < 360; j += 90 / precision) {
            List<FastVec3f> points = new ArrayList<>();
            float y = (float) (centerRadius + radius * Math.cos(FastLibMathUtils.degreeToRadians(j)));
            for (int i = 0; i <= 360; i += 90 / precision) {
                float degree = FastLibMathUtils.degreeToRadians(i);
                points.add(center.add(
                        (float) (y * Math.cos(degree)), (float) (radius * Math.sin(FastLibMathUtils.degreeToRadians(j))), (float) (y * Math.sin(degree))));
            }
            for (FastVec3f point : points) {
                Minecraft.getInstance().particleEngine.createParticle(
                        particleOptions,
                        point.x(), point.y(), point.z(),
                        0, 0, 0
                );
            }
        }
    }
}
