package com.robson.fastlib.api.data.reloader;

import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonCompressor {

    private static final Path OUTPUT_DIR = Minecraft.getInstance().gameDirectory.toPath().resolve("fastlib_data");

    public static String compress(String dataType, String modid, String name, JsonElement jsonElement) {
        if (dataType == null || dataType.isEmpty() || modid == null || modid.isEmpty() || name == null || name.isEmpty() || jsonElement == null) {
            return "";
        }
        Path outputDir = OUTPUT_DIR.resolve(dataType);
        if (!Files.exists(outputDir)) {
            try {
                Files.createDirectories(outputDir);
            }
            catch (IOException e) {
               throw new RuntimeException(e);
            }
        }

        if (jsonElement.isJsonNull()) {
            throw new RuntimeException("empty or invalid json: " + outputDir);
        }

        try {
            CompoundTag tag = TagParser.parseTag(jsonElement.toString());
            String filename = modid + ":" + name;
            File outPath = outputDir.resolve(name.replace(dataType + "/", "") + ".dat").toFile();
            try {
                NbtIo.write(tag, outPath);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            return filename;
        }
        catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
