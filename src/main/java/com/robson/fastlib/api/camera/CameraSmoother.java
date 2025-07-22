package com.robson.fastlib.api.camera;

public interface CameraSmoother<T> {

    void setTarget(T target);

    void update(float deltaTime);

    T getCurrent();

}
