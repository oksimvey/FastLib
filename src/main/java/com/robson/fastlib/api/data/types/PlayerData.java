package com.robson.fastlib.api.data.types;

import com.robson.fastlib.api.camera.CustomCam;
import com.robson.fastlib.api.utils.CamUtils;
import com.robson.fastlib.api.utils.math.FastVec3f;

public class PlayerData {


    private CustomCam camera;


    public PlayerData() {
        this.camera = new CustomCam();
    }

    public CustomCam getCamera() {
        return camera;
    }

}
