package com.robson.fastlib.api.data.structures;

import com.robson.fastlib.api.data.file.types.DataInstance;

public class FastStaticMap<T extends DataInstance> extends FastTemporaryMap<Short, T> {

    public FastStaticMap() {
        super(FastDataParameter.DataType.GENERIC_DATA);
    }
}
