package com.robson.fastlib.api.data.file.types;

public class ItemData extends DataInstance{

    private final String name;

    private final byte stacks;

    public ItemData(String name, byte stacks) {
        this.name = name;
        this.stacks = stacks;
    }

    @Override
    public int getSize() {
        return 10;
    }

    public String getName() {
        return name;
    }

    public byte getStacks() {
        return stacks;
    }

}
