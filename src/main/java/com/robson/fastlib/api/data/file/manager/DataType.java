package com.robson.fastlib.api.data.file.manager;
import com.robson.fastlib.api.data.file.types.DataInstance;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Objects;

public abstract class DataType<T extends DataInstance>{

    private final Path defaultPath;

    public DataType(Path defaultPath) {
        this.defaultPath = Objects.requireNonNull(defaultPath);
    }

    public final byte[] write(Object data) {
        return write((T) (Object)data);
    }

    protected abstract byte[] write(T data);

    public abstract T read(byte[] data);

   public abstract Class<T> getDataClass();



    public void compress(Path inputdir, Path outputdir){
        try {
            DataSerializer.convertJsonDirectoryToFastdata(inputdir, outputdir, this);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public T read(String file) {
        try {
             return read(DataSerializer.getBytes(defaultPath.resolve(file)));
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Path getDefaultPath() {
        return defaultPath;
    }
}
