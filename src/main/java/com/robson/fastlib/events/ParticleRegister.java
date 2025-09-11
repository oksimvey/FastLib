package com.robson.fastlib.events;


import com.robson.fastlib.api.client.CustomParticle;
import com.robson.fastlib.api.client.EntityAsParticle;
import com.robson.fastlib.main.FastLib;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.client.particle.EntityAfterimageParticle;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleRegister {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, FastLib.MOD_ID);

    public static final RegistryObject<SimpleParticleType> RED_LIGHTNING = PARTICLES.register("custom_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> ENTITY = PARTICLES.register("entity", () -> new SimpleParticleType(true));

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(RED_LIGHTNING.get(), CustomParticle.Provider::new);
        event.registerSpecial(ENTITY.get(), new EntityAsParticle.WhiteAfterimageProvider());
    }
}