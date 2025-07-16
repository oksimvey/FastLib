package com.robson.fastlib.api;

import net.minecraft.client.Minecraft;

import java.nio.file.Path;

public class DataFileManager {

    public static final Path OUTPUT = Minecraft.getInstance().gameDirectory.toPath().resolve("pride_data");

    public static final Path DYNAMIC_MAP_OUTPUT = OUTPUT.resolve("dynamic");

}
