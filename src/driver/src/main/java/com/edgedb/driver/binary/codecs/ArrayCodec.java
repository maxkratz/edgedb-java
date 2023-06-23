package com.edgedb.driver.binary.codecs;

import com.edgedb.driver.binary.PacketReader;
import com.edgedb.driver.binary.PacketWriter;
import com.edgedb.driver.exceptions.EdgeDBException;
import com.edgedb.driver.util.BinaryProtocolUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.Array;

public class ArrayCodec<T> extends CodecBase<T[]> {
    private static final byte[] EMPTY_ARRAY = new byte[] {
            0,0,0,0,
            0,0,0,0,
            0,0,0,0,
            0,0,0,0,
            0,0,0,1
    };

    private final Codec<T> innerCodec;

    @SuppressWarnings("unchecked")
    public ArrayCodec(Class<?> cls, Codec<?> codec) {
        super((Class<T[]>) cls);
        this.innerCodec = (Codec<T>) codec;
    }

    @Override
    public void serialize(@NotNull PacketWriter writer, T @Nullable [] value, CodecContext context) throws OperationNotSupportedException, EdgeDBException {
        if(value == null) {
            writer.writeArrayWithoutLength(EMPTY_ARRAY);
            return;
        }

        writer.write(1); // num dimensions
        writer.write(0); // reserved
        writer.write(0); // reserved

        // dimensions: length for upper, 1 for lower
        writer.write(value.length);
        writer.write(1);

        for(int i = 0; i != value.length; i++) {
            var element = value[i];

            if(element == null) {
                writer.write(-1);
            } else {
                writer.writeDelegateWithLength((w) -> this.innerCodec.serialize(w, element, context));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T @Nullable [] deserialize(@NotNull PacketReader reader, CodecContext context) throws EdgeDBException, OperationNotSupportedException {
        var dimensions = reader.readInt32();

        reader.skip(BinaryProtocolUtils.LONG_SIZE); // reserved

        if(dimensions == 0) {
            return (T[])Array.newInstance(innerCodec.getConvertingClass(), 0);
        }

        var upper = reader.readInt32();
        var lower = reader.readInt32();

        var numElements = upper - lower + 1;

        var array = (T[])Array.newInstance(innerCodec.getConvertingClass(), numElements);

        for(int i = 0; i != numElements; i++) {
            array[i] = reader.deserializeByteArray(innerCodec, context);
        }

        return array;
    }
}