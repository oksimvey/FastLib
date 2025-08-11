package com.robson.fastlib.api.data.file.manager;

import com.robson.fastlib.api.data.file.types.DataInstance;

public interface IDataManager<T extends DataInstance>  {

    T getDefault(short id);

}
