package com.robson.fastlib.api.data.file.manager;

import com.esotericsoftware.kryo.Serializer;
import com.robson.fastlib.api.data.file.types.DataInstance;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Objects;

public final class DataType<T extends DataInstance> {

    private final Class<T> type;

    private final Path defaultPath;

    public DataType(Class<T> type,
                     Serializer<T> serializer,
                     Path defaultPath) {
        this.type = Objects.requireNonNull(type);
        this.defaultPath = Objects.requireNonNull(defaultPath);
        DataSerializer.registerKryo(type, serializer);
    }

    public void compress(Path inputdir, Path outputdir){
        try {
            DataSerializer.convertJsonDirectoryToFastdata(inputdir, outputdir, type);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public T read(String file) {
        try {
            return DataSerializer.fromFastdata(defaultPath.resolve(file), type);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Path getDefaultPath() {
        return defaultPath;
    }
}
