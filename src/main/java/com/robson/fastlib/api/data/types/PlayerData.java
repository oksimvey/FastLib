package com.robson.fastlib.api.data.types;

import com.robson.fastlib.api.utils.CamUtils;
import com.robson.fastlib.api.utils.math.CutsceneUtils;
import com.robson.fastlib.api.utils.math.FastVec3f;

public class PlayerData {

    public static float acumulateddx = 0;
    public static float accumulateddy = 0;

    public CutsceneUtils.Cutscene currentCutscene;

    public CamUtils.CameraParameter currentCamera;

    public PlayerData() {
        currentCamera = new CamUtils.CameraParameter(0 ,0 ,0 ,0, 0);
    }

    public FastVec3f getCameraOffset(){
        if (currentCutscene == null) {
            return new FastVec3f(0, 1, 0);
        }
        return new FastVec3f(0, 0, 0);
    }
}
