package com.robson.fastlib.api.data.file.manager;

import com.robson.fastlib.api.data.file.types.ItemData;

public interface ItemDataManager {

    byte EUROPEAN_LONGSWORD = -128;

    DataManager<ItemData> ITEMS = new DataManager<>() {
        
        @Override
        public ItemData getDefault(short id) {
            return switch (id){
                case EUROPEAN_LONGSWORD -> new ItemData();
                default -> null;
            };
        }
    };

}
