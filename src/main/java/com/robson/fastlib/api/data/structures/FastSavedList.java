package com.robson.fastlib.api.data.structures;

public class FastSavedList<A> extends FastSavedMap<A, Boolean>{

    public FastSavedList(FastDataParameter.DataType keytype) {
        super(keytype, FastDataParameter.DataType.BOOLEAN);
    }
}
