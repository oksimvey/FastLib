package com.robson.fastlib.api.camera;

public interface CameraSmoother<T> {

    void setCurrent(T current);

    void setTarget(T target);

    void update(float deltaTime);

    T getCurrent();

}
