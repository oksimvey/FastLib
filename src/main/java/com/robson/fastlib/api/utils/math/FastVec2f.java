package com.robson.fastlib.api.utils.math;

public record FastVec2f(float x, float y) {
    public static final FastVec2f ZERO = new FastVec2f(0, 0);

    public FastVec2f add(FastVec2f vec2f){
        return new FastVec2f(x + vec2f.x, y + vec2f.y);
    }

    public FastVec2f add(float x, float y){
        return new FastVec2f(this.x + x, this.y + y);
    }

    public FastVec2f sub(FastVec2f vec2f){
        return new FastVec2f(x - vec2f.x, y - vec2f.y);
    }

    public FastVec2f sub(float x, float y){
        return new FastVec2f(this.x - x, this.y - y);
    }

    public FastVec2f scale(float scale){
        return new FastVec2f(x * scale, y * scale);
    }

    public FastVec2f divide(float scale){
        if (scale == 0) return new FastVec2f(0 ,0);
        return new FastVec2f(x / scale, y / scale);
    }

    public float dot(FastVec2f vec2f){
        return (x * vec2f.x) + (y * vec2f.y);
    }

    public FastVec2f cross(FastVec2f vec2f){
        return new FastVec2f(y * vec2f.y - x * vec2f.x, x * vec2f.x - y * vec2f.y);
    }

    public float length(){
        return (float) Math.sqrt((x * x) + (y * y));
    }

    public FastVec2f normalize(){
        return divide(length());
    }

    public FastVec2f rotate(float angle){
        return FastLibMathUtils.rotate2DVector(this, angle);
    }
}
