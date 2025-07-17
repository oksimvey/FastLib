package com.robson.fastlib.api.utils.math;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class BezierCurve {

    private static final FastFloatList MATRIX_CONSTANTS = new FastFloatList();

    static {
        MATRIX_CONSTANTS.add(0.5F);
    }

    private static void getBezierEquationCoefficients(FastFloatList points, FastFloatList aList, FastFloatList bList) {
        var results = new FastFloatList();
        int size = points.size();
        results.add(points.get(0) + points.get(1) * 2.0F);

        for (int idx = 1; idx < size - 2; ++idx) {
            results.add(points.get(idx) * 4.0F + points.get(idx + 1) * 2.0F);
        }

        results.add(points.get(size - 2) * 8.0F + points.get(size - 1));
        int storedConstsSize = MATRIX_CONSTANTS.size();
        int coordSize = results.size();
        if (storedConstsSize < coordSize - 1) {
            for (int i = 0; i < coordSize - 1 - storedConstsSize; ++i) {
                float lastConst = MATRIX_CONSTANTS.get(MATRIX_CONSTANTS.size() - 1);
                MATRIX_CONSTANTS.add(1.0F / (4.0F - lastConst));
            }
        }
        var convertedResults = new FastFloatList();
        for (int idx = 0; idx < coordSize; ++idx) {
            if (idx == 0) {
                convertedResults.add(results.get(idx) * 0.5F);
            } else if (idx == coordSize - 1) {
                convertedResults.add((results.get(idx) - 2.0F * convertedResults.get(idx - 1)) / (7.0F - 2.0F * MATRIX_CONSTANTS.get(idx - 1)));
            } else {
                convertedResults.add((results.get(idx) - convertedResults.get(idx - 1)) / (4.0F - MATRIX_CONSTANTS.get(idx - 1)));
            }
        }

        for (int idx = coordSize - 1; idx >= 0; --idx) {
            if (idx == coordSize - 1) {
                aList.add(0, convertedResults.get(idx));
            } else {
                aList.add(0, convertedResults.get(idx) - aList.get(0) * MATRIX_CONSTANTS.get(idx));
            }
        }

        for (int i = 0; i < coordSize; ++i) {
            if (i == coordSize - 1) {
                bList.add((aList.get(i) + points.get(i + 1)) * 0.5F);
            } else {
                bList.add(2.0F * points.get(i + 1) - aList.get(i + 1));
            }
        }

    }

    private static float cubicBezier(float start, float end, float a, float b, float t) {
        return (float) (Math.pow(1.0F - t, 3.0F) * start + 3.0F * t * Math.pow(1.0F - t, 2.0F) * a + 3.0F * t * t * (1.0F - t) * b + t * t * t * end);
    }

    public static List<FastVec3f> getBezierInterpolatedPoints(List<FastVec3f> points, int interpolatedResults) {
        return getBezierInterpolatedPoints(points, 0, points.size() - 1, interpolatedResults);
    }

    public static List<FastVec3f> getBezierInterpolatedPoints(List<FastVec3f> points, int sliceBegin, int sliceEnd, int interpolatedResults) {
        if (points.size() < 3) {
            return null;
        } else {
            sliceBegin = Math.max(sliceBegin, 0);
            sliceEnd = Math.min(sliceEnd, points.size() - 1);
            var interpolatedPoints = new ArrayList<FastVec3f>();
            var x = new FastFloatList();
            var y = new FastFloatList();
            var z = new FastFloatList();

            for (FastVec3f point : points) {
                x.add(point.x());
                y.add(point.y());
                z.add(point.z());
            }

           var x_a = new FastFloatList();
            var x_b = new FastFloatList();
            var y_a = new FastFloatList();
            var y_b = new FastFloatList();
            var z_a = new FastFloatList();
            var z_b = new FastFloatList();
            getBezierEquationCoefficients(x, x_a, x_b);
            getBezierEquationCoefficients(y, y_a, y_b);
            getBezierEquationCoefficients(z, z_a, z_b);

            for (int i = sliceBegin; i < sliceEnd; ++i) {
                if (!interpolatedPoints.isEmpty()) {
                    interpolatedPoints.remove(interpolatedPoints.size() - 1);
                }

                var start = points.get(i);
                var end = points.get(i + 1);
                float x_av = x_a.get(i);
                float x_bv = x_b.get(i);
                float y_av = y_a.get(i);
                float y_bv = y_b.get(i);
                float z_av = z_a.get(i);
                float z_bv = z_b.get(i);

                for (int j = 0; j < interpolatedResults + 1; ++j) {
                    float t = (float) j / interpolatedResults;
                    interpolatedPoints.add(new FastVec3f(cubicBezier(start.x(), end.x(), x_av, x_bv, t), cubicBezier(start.y(), end.y(), y_av, y_bv, t), cubicBezier(start.z(), end.z(), z_av, z_bv, t)));
                }
            }

            return interpolatedPoints;
        }
    }
}
