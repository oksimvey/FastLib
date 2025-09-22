package com.robson.fastlib.fastlibevents;

import com.robson.fastlib.api.events.types.OnRenderGUIEvent;

public class GUIEvents {

    public static void registerEvents(){

        OnRenderGUIEvent.EVENT_MANAGER.registerEvent(new OnRenderGUIEvent() {
            @Override
            public boolean canTick(Context args) {
                return true;
            }

            @Override
            public void onTick(Context args) {

            }
        });
    }
}
