package com.robson.fastlib.api.camera;

import com.robson.fastlib.api.utils.math.BezierCurve;
import com.robson.fastlib.api.utils.math.FastVec2f;
import com.robson.fastlib.api.utils.math.FastVec3f;

import java.util.ArrayList;
import java.util.List;

public class Cutscene {

    public enum Type {
        LOCAL,
        GLOBAL
    }

    private  List<CutsceneKeyFrame> keyFrames;

    private final Type type;

    private final float duration;

    public Cutscene(Type type, List<CutsceneKeyFrame> keyFrames, float duration, int interpolation) {
        this.type = type;
        this.keyFrames = new ArrayList<>();
        this.duration = duration;
        List<FastVec3f> curve = new ArrayList<>();
        List<FastVec3f> rotCurve = new ArrayList<>();
        for (CutsceneKeyFrame keyFrame : keyFrames) {
            FastVec3f position = keyFrame.getPosition();
            curve.add(position);
            rotCurve.add(new FastVec3f(keyFrame.getRotation().x(), keyFrame.getRotation().y(), 0f));
        }
        curve = BezierCurve.getBezierInterpolatedPoints(curve, interpolation);
        rotCurve = BezierCurve.getBezierInterpolatedPoints(rotCurve, interpolation);
        for (int i = 0; i < curve.size(); i++) {
            this.keyFrames.add(new CutsceneKeyFrame(curve.get(i), new FastVec2f(rotCurve.get(i).x(), rotCurve.get(i).y()) ) );
        }

    }

    public float getDuration() {
        return duration;
    }

    public Type getType() {
        return type;
    }

    public List<CutsceneKeyFrame> getKeyFrames() {
        return keyFrames;
    }

    public static class CutsceneKeyFrame {

        private final FastVec3f position;

        private final FastVec2f rotation;

        public CutsceneKeyFrame(FastVec3f position, FastVec2f rotation) {
            this.position = position;
            this.rotation = rotation;
        }

        public CutsceneKeyFrame(FastVec3f position) {
            this.position = position;
            this.rotation = FastVec2f.ZERO;
        }

        public FastVec3f getPosition() {
            return position;
        }

        public FastVec2f getRotation() {
            return rotation;
        }


    }
}
