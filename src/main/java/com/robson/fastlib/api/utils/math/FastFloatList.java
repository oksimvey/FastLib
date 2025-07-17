package com.robson.fastlib.api.utils.math;

public class FastFloatList {

    private float[] list;

    public FastFloatList(float... list){
       this. list =  list;
    }

    public void add(int index, float value){
        if (index >= list.length) return;
        float[] newList = new float[list.length + 1];
        for (int i = 0; i < list.length; i++) {
            if (i == index) {
                newList[i] = value;
                continue;
            }
            newList[i] = list[i];
        }
    }

    public void add(float value){
        this.list = new float[list.length + 1];
        this.list[list.length - 1] = value;
    }

    public boolean contains(float value){
        for (float f : list) {
            if (f == value) return true;
        }
        return false;
    }

    public void removeByIndex(int index){
        if (index >= list.length) return;
        float[] newList = new float[list.length - 1];
        for (int i = 0; i < list.length; i++) {
            if (i == index) continue;
            newList[i] = list[i];
        }
        this.list = newList;
    }

    public void removeByValue(float value){
        for (int i = 0; i < list.length; i++) {
            if (list[i] == value) removeByIndex(i);
        }
    }

    public int size(){
        return list.length;
    }

    public float get(int index){
        if (index >= list.length) return 0;
        return list[index];
    }
}
