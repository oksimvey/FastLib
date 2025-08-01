package com.robson.fastlib.api.data.file.types;

public class ItemData extends DataInstance{

    private final String name;

    private final byte stacks;

    private final float[] arr;

    public ItemData(String name, byte stacks, float[] arr) {
        this.name = name;
        this.stacks = stacks;
        this.arr = arr;
    }


    @Override
    public int getSize() {
        return 10;
    }

    public float[] getArr() {
        return arr;
    }

    public String getName() {
        return name;
    }

    public byte getStacks() {
        return stacks;
    }

    public static class InnerItemData extends DataInstance{

        private final String name;

        public InnerItemData(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public int getSize() {
            return 10;
        }
    }

}
