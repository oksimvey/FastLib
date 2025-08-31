package com.robson.fastlib.main;

import com.robson.fastlib.api.events.manager.PlayerCustomTickManager;
import com.robson.fastlib.api.events.types.FastLibPlayerEvent;
import com.robson.fastlib.api.events.types.OnRenderPatchedEntityEvent;
import com.robson.fastlib.events.ParticleRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.concurrent.ThreadLocalRandom;

@Mod("fastlib")
public class FastLib {

    public static final String MOD_ID = "fastlib";


    public FastLib(FMLJavaModLoadingContext context) {
        final IEventBus bus = context.getModEventBus();
        bus.addListener(this::init);
        ParticleRegister.PARTICLES.register(bus);
    }

    private void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

            OnRenderPatchedEntityEvent.EVENT_MANAGER.registerEvent(new OnRenderPatchedEntityEvent() {
                @Override
                public byte getFlag() {
                    return 5;
                }

                @Override
                public boolean canTick(Context args) {
                    return true;
                }

                @Override
                public void onTick(Context args) {
                    if (!(args.entity() instanceof Player)) return;


                    float offsetx = ThreadLocalRandom.current().nextFloat(1.5f) - 0.75f;
                    float offsetz = ThreadLocalRandom.current().nextFloat(1.5f) - 0.75f;
                    float offsety = ThreadLocalRandom.current().nextFloat(1.5f) - 0.75f;
                    Minecraft.getInstance().particleEngine.createParticle(ParticleRegister.RED_LIGHTNING.get(),
                            args.entity().getX() + offsetx, args.entity().getY() + 0.5 + offsety, args.entity().getZ() + offsetz,
                            0 +args.entity().getDeltaMovement().x , 0.25,
                            args.entity().getDeltaMovement().z);
                    if (args.poses() == null) return;
                ;}
            });


            PlayerCustomTickManager.EVENT_MANAGER.registerEvent(new FastLibPlayerEvent() {


                @Override
                public boolean canTick(Context args) {
                    return true;
                }

                @Override
                public void onTick(Context args) {
               /*
                final Vec3 oldpos = args.player().position();
                Scheduler.schedule(()-> {
                    FastVec2f delta = new FastVec2f((float) (args.player().getX() - oldpos.x), (float) (args.player().getZ() - oldpos.z));
                float zmodifier =  0.2f + (delta.length());
                    for (int i = 0; i < 10; i++) {
                        if (args.player().level().getBlockState(args.player().blockPosition().offset(0, i, 0)).isSolid()) {
                            zmodifier -= zmodifier * (1f / (minimum(i) * 0.15f));
                            break;
                        }
                    }
                float xmodifier = -0.35f + (zmodifier / 50);
                float ymodifier = 0.4f;
                args.playerData().getCamera().setPos(new FastVec3f(xmodifier, ymodifier, -zmodifier));
            }, 100, TimeUnit.MILLISECONDS);

                */
                }

                @Override
                public byte getFlag() {
                    return 5;
                }
            });
        });
    }

    static int minimum(int i){
        return i == 0 ? 1 : i;
    }
}
