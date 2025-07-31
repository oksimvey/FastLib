package com.robson.fastlib.api.data.file.manager;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.io.Input;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataSerializer {

     static final Kryo kryo = new Kryo();

   public static void registerKryo(Class<?> clazz, Serializer<?> serializer){
       kryo.register(clazz, serializer);
    }

    public static byte[] toBytes(Object obj) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Output output = new Output(stream);
        kryo.writeObject(output, obj); // Use writeObject para melhor controle
        output.close();
        return stream.toByteArray();
    }

    public static <T> T fromFastdata(Path fastdataFile, Class<T> targetClass) throws IOException {
        byte[] bytes = Files.readAllBytes(fastdataFile);
        Input input = new Input(new ByteArrayInputStream(bytes));
        T obj = kryo.readObject(input, targetClass);
        input.close();
        return obj;
    }

    private static final Gson gson = new Gson();

    public static void convertJsonDirectoryToFastdata(Path inputDir, Path outputDir, Class<?> targetClass) throws IOException {
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir, "*.json")) {
            for (Path jsonFile : stream) {
                String json = Files.readString(jsonFile);
                Object obj = gson.fromJson(json, targetClass);

                byte[] data = DataSerializer.toBytes(obj);

                String fileName = jsonFile.getFileName().toString().replace(".json", ".fastdata");
                Path outputFile = outputDir.resolve(fileName);

                Files.write(outputFile, data);
                System.out.println("Convertido: " + jsonFile.getFileName() + " -> " + fileName);
            }
        }
    }
}
