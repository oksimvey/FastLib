package com.robson.fastlib.api.data.file.manager;

import com.robson.fastlib.api.data.file.types.DataInstance;
import com.robson.fastlib.api.data.structures.FastStaticMap;

public abstract class DataManager<T extends DataInstance> implements IDataManager<T>{

    private final FastStaticMap<T> DATA = new FastStaticMap<>();

    public final T getByKey(short id) {
        if (DATA.get(id) == null) {
            T value = getDefault(id);
            if (value != null) {
                DATA.put(id, value);
                return value;
            }
        }
        return DATA.get(id);
    }

}
