package com.robson.fastlib.api.utils.math;

public record FastVec4f(float x, float y, float z, float w) {

    public static final FastVec4f ZERO = new FastVec4f(0, 0, 0, 0);

}
