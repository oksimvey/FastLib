package com.robson.fastlib.api.data.file.manager;

import com.robson.fastlib.api.data.file.types.DataInstance;
import com.robson.fastlib.api.data.file.types.ItemData;
import com.robson.fastlib.api.utils.math.BinaryUtils;
import org.agrona.BitUtil;
import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

public interface DataTypes {

    DataType<ItemData> ITEMS = new DataType<>(Path.of("src/main/resources/new")) {

        @Override
        public byte[] write(ItemData data) {
            BinaryUtils writer = new BinaryUtils(256); // Tamanho inicial estimado

            writer.writeString(data.getName())         // Campo "name"
                    .writeByte(data.getStacks())         // Campo "stacks"
                    .writeFloatArray(data.getArr());

            return writer.toByteArray();
        }
        @Override
        public ItemData read(byte[] data) {
            BinaryUtils reader = new BinaryUtils(data);

            return new ItemData(
                    reader.readString(),       // Campo "name"
                    reader.readByte(),         // Campo "stacks"
                    reader.readFloatArray());
        }
        @Override
        public Class<ItemData> getDataClass() {
            return ItemData.class;
        }
    };
}
