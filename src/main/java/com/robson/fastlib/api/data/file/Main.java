package com.robson.fastlib.api.data.file;

import com.robson.fastlib.api.data.file.manager.DataTypes;
import com.robson.fastlib.api.data.file.types.ItemData;

import java.io.File;
import java.nio.file.Path;

public class Main {

    static Path path = Path.of("src/main/resources/dados");

    static Path to = Path.of("src/main/resources/new");

    public static void main(String[] args) {

    }

    static void write() {
        DataTypes.ITEMS.compress(path, to);
    }

    private static void read() {
        System.out.println("running");
        ItemData data = DataTypes.ITEMS.read("blade.fastdata");

        System.out.println(data.getName());
        System.out.println(data.getStacks());

    }
}
