package com.robson.fastlib.api.data.structures;

import com.robson.fastlib.api.GenericData;

public class FastDataParameter<A> {

    private static final byte BYTE_MODIFIER = 5;

    public enum DataType {
        BOOLEAN,
        STRING,
        INTEGER,
        FLOAT,
        SHORT,
        GENERIC_DATA,
        ITEM_STACK,
        ENTITY,
        VECTORS
    }

    public final A data;

    public int accesses;

    public long lastUpdate;

    public final int size;

    public FastDataParameter(A data, DataType type) {
        this.data = data;
        this.accesses = 0;
        this.lastUpdate = System.currentTimeMillis();
        this.size = switch (type) {

            case BOOLEAN -> 1 << BYTE_MODIFIER;

            case SHORT -> 16 << BYTE_MODIFIER;

            case INTEGER, FLOAT, VECTORS -> 32 << BYTE_MODIFIER;

            case ITEM_STACK -> 64 << BYTE_MODIFIER;

            case ENTITY -> 3000;

            case STRING -> ((String) data).length() << BYTE_MODIFIER;

            case GENERIC_DATA -> ((GenericData) data).getSize();

        };
    }


    public A getData() {
        accesses++;
        return data;
    }

    public void resetAccesses() {
        this.lastUpdate = System.currentTimeMillis();
        this.accesses = 0;
    }
}
