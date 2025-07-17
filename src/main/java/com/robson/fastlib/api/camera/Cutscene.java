package com.robson.fastlib.api.camera;

import com.robson.fastlib.api.utils.CamUtils;
import com.robson.fastlib.api.utils.LoopUtils;
import com.robson.fastlib.api.utils.math.BezierCurve;
import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;

import java.util.ArrayList;
import java.util.List;

public class Cutscene {

    private final int DURATION;

    private final List<CamUtils.CameraParameter> KEYFRAMES;

    private CamUtils.CameraParameter currentKeyframe;

    public Cutscene(int duration, CamUtils.CameraParameter... keyframes) {
        DURATION = duration;
        KEYFRAMES = List.of(keyframes);
    }

    public void start(){
        var positions = new ArrayList<FastVec3f>();
        var rotations = new ArrayList<FastVec2f>();
        for (var keyframe : KEYFRAMES) {
            positions.add(keyframe.getPosition());
            rotations.add(keyframe.getRotation());
        }
        var interpolated = BezierCurve.getBezierInterpolatedPoints(positions, 5);
        var interpolatedrot = CreateSmoothRotation.createSmoothRotation(rotations, interpolated.size());
        int interval = DURATION / interpolated.size();
        LoopUtils.loopByTimes( i -> {
            this.currentKeyframe = CamUtils.createParameter(interpolated.get(i), interpolatedrot.get(i));
        }, interpolated.size(), interval);
    }

    public CamUtils.CameraParameter getCurrentKeyframe() {
        return currentKeyframe != null ? currentKeyframe : null;
    }
}
