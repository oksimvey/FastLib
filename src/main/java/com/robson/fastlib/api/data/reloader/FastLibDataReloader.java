package com.robson.fastlib.api.data.reloader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FastLibDataReloader extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY = "fastlib_data";

    private static final Gson GSON = (new GsonBuilder()).create();

    public FastLibDataReloader() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profileIn) {
        return super.prepare(resourceManager, profileIn);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        List<String> directories = getStrings(objectIn);
        List<String> registries = new ArrayList<>();
        for (String dir : directories) {
            System.out.println("Diretório: " + dir);

            for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
                ResourceLocation rl = entry.getKey();
                String path = rl.getPath();

                if (path.startsWith(dir + "/")) {
                    System.out.println("  Arquivo: " + path);
                    JsonElement json = entry.getValue();

                    String registry = JsonCompressor.compress(dir, rl.getNamespace(), path, json);
                    if (!registry.isEmpty()) {
                        registries.add(registry);
                    }
                }
            }
        }

    }

    private static @NotNull List<String> getStrings(Map<ResourceLocation, JsonElement> objectIn) {
        List<String> directories = new ArrayList<>();
        for (ResourceLocation rl : objectIn.keySet()) {
            String path = rl.getPath();
            int lastSlash = path.lastIndexOf('/');
            if (lastSlash != -1) {
                String dir = path.substring(0, lastSlash);
                if (!directories.contains(dir)) {
                    directories.add(dir);
                }
            }
        }
        return directories;
    }

}
