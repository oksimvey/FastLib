package com.robson.fastlib.api.data.structures;

public class FastTemporaryList<A> extends FastTemporaryMap<A, Boolean>{

    public FastTemporaryList(FastDataParameter.DataType type) {
        super(FastDataParameter.DataType.BOOLEAN);
    }

    public void add(A a){
        super.put(a, true);
    }
}
