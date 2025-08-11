package com.robson.fastlib.main;

import com.robson.fastlib.api.events.manager.PlayerCustomTickManager;
import com.robson.fastlib.api.events.types.FastLibPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("fastlib")
public class FastLib {

    public static final String MOD_ID = "fastlib";

    public FastLib(FMLJavaModLoadingContext context) {
        context.getModEventBus().addListener(this::init);
    }

    private void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> PlayerCustomTickManager.EVENT_MANAGER.registerEvent(new FastLibPlayerEvent() {


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
        }));
    }

    static int minimum(int i){
        return i == 0 ? 1 : i;
    }
}
