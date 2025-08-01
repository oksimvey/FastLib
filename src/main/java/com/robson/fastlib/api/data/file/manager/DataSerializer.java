package com.robson.fastlib.api.data.file.manager;

import com.google.gson.Gson;
import com.robson.fastlib.api.data.file.types.DataInstance;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataSerializer {


    public static byte[] getBytes(Path fastdataFile) throws IOException {
        return Files.readAllBytes(fastdataFile);
    }

    static Gson gson = new Gson();

    public static <T extends DataInstance> void convertJsonDirectoryToFastdata(
            Path inputDir,
            Path outputDir,
            DataType<T> dataType) throws IOException {

        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir, "*.json")) {
            for (Path jsonFile : stream) {
                String json = Files.readString(jsonFile, StandardCharsets.UTF_8);
                T item = gson.fromJson(json, dataType.getDataClass());
                byte[] data = dataType.write(item);
                String fileName = jsonFile.getFileName().toString().replace(".json", ".fastdata");
                Path outputFile = outputDir.resolve(fileName);

                Files.write(outputFile, data);
                System.out.println("Convertido: " + jsonFile.getFileName() + " -> " + fileName);
            }
        }
    }

}
