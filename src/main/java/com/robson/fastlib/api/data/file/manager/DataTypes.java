package com.robson.fastlib.api.data.file.manager;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.robson.fastlib.api.data.file.types.ItemData;

import java.nio.file.Path;

public interface DataTypes {

    DataType<ItemData> ITEMS = new DataType<>(ItemData.class,
            new Serializer<>() {
                @Override
                public void write(Kryo kryo, Output output, ItemData itemData) {
                    output.writeString(itemData.getName());
                    output.writeByte(itemData.getStacks());
                }

                @Override
                public ItemData read(Kryo kryo, Input input, Class<? extends ItemData> aClass) {
                    String name = input.readString();
                    byte stacks = input.readByte();
                    return new ItemData(name, stacks);
                }
            }, Path.of("src/main/resources/new"));
}
