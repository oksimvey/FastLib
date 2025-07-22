package com.robson.fastlib.api.camera;

import com.robson.fastlib.api.utils.LoopUtils;
import com.robson.fastlib.api.utils.math.BezierCurve;
import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;

import java.util.ArrayList;
import java.util.List;

public class Cutscene {

    public record CameraKeyFrame(FastVec3f position, FastVec2f rotation){}

    private final int DURATION;

    private final List<CameraKeyFrame> KEYFRAMES;

    private CameraKeyFrame currentKeyframe;

    private boolean started;

    public Cutscene(int duration, CameraKeyFrame... keyframes) {
        DURATION = duration;
        KEYFRAMES = List.of(keyframes);
        started = false;
    }

    public void start() {
        started = true;
        var interpolated = new ArrayList<CameraKeyFrame>();
        var pos = new ArrayList<FastVec3f>();
        for (CameraKeyFrame frame : KEYFRAMES) {
            pos.add(frame.position);
        }
    }
    public CameraKeyFrame getCurrentKeyframe() {
        return currentKeyframe;
    }
}
