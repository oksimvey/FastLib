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

    public Cutscene(int duration, CameraKeyFrame... keyframes) {
        DURATION = duration;
        KEYFRAMES = List.of(keyframes);
        start();
    }

    public void start(){
        var interpolated = new ArrayList<CameraKeyFrame>();
        var pos = new ArrayList<FastVec3f>();
        for (CameraKeyFrame frame : KEYFRAMES){
            pos.add(frame.position);
        }
        var newpos = BezierCurve.getBezierInterpolatedPoints(pos, 5);
        for (FastVec3f vec3f: newpos){
            interpolated.add(new CameraKeyFrame(vec3f, FastVec2f.ZERO));
        }
        int interval = DURATION / interpolated.size();
        LoopUtils.loopByTimes( i -> {
            currentKeyframe = interpolated.get(i);
        }, interpolated.size(), interval);
    }

    public CameraKeyFrame getCurrentKeyframe() {
        return currentKeyframe;
    }
}
