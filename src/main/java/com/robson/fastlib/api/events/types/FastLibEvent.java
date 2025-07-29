package com.robson.fastlib.api.events.types;

public abstract class FastLibEvent<A> {

    public abstract boolean canTick(A args);

    public abstract void onTick(A args);

}

