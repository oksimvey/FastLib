package com.robson.fastlib.api.utils.math;

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BinaryUtils {

    private static final ByteOrder DEFAULT_ORDER = ByteOrder.BIG_ENDIAN;
    private final ByteOrder byteOrder;
    private final MutableDirectBuffer buffer;
    private int offset;
    private final List<FieldDescriptor> schema = new ArrayList<>();

    public BinaryUtils(int capacity) {
        this(new UnsafeBuffer(new byte[capacity]), 0, DEFAULT_ORDER);
    }

    public BinaryUtils(MutableDirectBuffer buffer, int offset, ByteOrder byteOrder) {
        this.buffer = buffer;
        this.offset = offset;
        this.byteOrder = byteOrder;
    }

    public BinaryUtils(byte[] data) {
        this(new UnsafeBuffer(data), 0, DEFAULT_ORDER);
    }

    // ---------- WRITE METHODS ----------
    public BinaryUtils writeByte(byte value) {
        buffer.putByte(offset, value);
        offset += Byte.BYTES;
        return this;
    }

    public BinaryUtils writeInt(int value) {
        buffer.putInt(offset, value, byteOrder);
        offset += Integer.BYTES;
        return this;
    }

    public BinaryUtils writeFloat(float value) {
        buffer.putFloat(offset, value, byteOrder);
        offset += Float.BYTES;
        return this;
    }

    public BinaryUtils writeString(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        writeInt(bytes.length);
        buffer.putBytes(offset, bytes);
        offset += bytes.length;
        return this;
    }

    public BinaryUtils writeFloatArray(float[] array) {
        writeInt(array.length);
        for (float value : array) {
            writeFloat(value);
        }
        return this;
    }

    public <T> BinaryUtils writeObject(T obj, BiConsumer<BinaryUtils, T> serializer) {
        serializer.accept(this, obj);
        return this;
    }

    // ---------- READ METHODS ----------
    public byte readByte() {
        byte value = buffer.getByte(offset);
        offset += Byte.BYTES;
        return value;
    }

    public int readInt() {
        int value = buffer.getInt(offset, byteOrder);
        offset += Integer.BYTES;
        return value;
    }

    public float readFloat() {
        float value = buffer.getFloat(offset, byteOrder);
        offset += Float.BYTES;
        return value;
    }

    public String readString() {
        int length = readInt();
        byte[] bytes = new byte[length];
        buffer.getBytes(offset, bytes);
        offset += length;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public float[] readFloatArray() {
        int length = readInt();
        float[] array = new float[length];
        for (int i = 0; i < length; i++) {
            array[i] = readFloat();
        }
        return array;
    }

    public <T> T readObject(Function<BinaryUtils, T> deserializer) {
        return deserializer.apply(this);
    }

    // ---------- SCHEMA-BASED SERIALIZATION ----------
    public BinaryUtils field(String name, BiConsumer<BinaryUtils, Object> writer, Function<BinaryUtils, Object> reader) {
        schema.add(new FieldDescriptor(name, writer, reader));
        return this;
    }

    public <T> void writeWithSchema(T obj) {
        schema.forEach(field -> field.writer.accept(this, obj));
    }

    public <T> T readWithSchema(T obj) {
        schema.forEach(field -> field.reader.apply(this));
        return obj;
    }

    // ---------- UTILITY METHODS ----------
    public int getOffset() {
        return offset;
    }

    public byte[] toByteArray() {
        byte[] result = new byte[offset];
        buffer.getBytes(0, result);
        return result;
    }

    public void reset() {
        offset = 0;
    }

    private record FieldDescriptor(String name, BiConsumer<BinaryUtils, Object> writer,
                                   Function<BinaryUtils, Object> reader) {
    }

    // ---------- STATIC UTILITIES ----------
    public static byte[] floatArrayToBytes(float[] floats) {
        ByteBuffer buffer = ByteBuffer.allocate(floats.length * Float.BYTES);
        buffer.order(DEFAULT_ORDER);
        buffer.asFloatBuffer().put(floats);
        return buffer.array();
    }

    public static float[] bytesToFloatArray(byte[] bytes) {
        FloatBuffer buffer = ByteBuffer.wrap(bytes)
                .order(DEFAULT_ORDER)
                .asFloatBuffer();
        float[] floats = new float[buffer.remaining()];
        buffer.get(floats);
        return floats;
    }
}