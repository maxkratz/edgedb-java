package com.edgedb.driver.binary.codecs.scalars;

import com.edgedb.driver.binary.PacketWriter;
import com.edgedb.driver.binary.codecs.CodecContext;
import com.edgedb.driver.binary.PacketReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.naming.OperationNotSupportedException;

public final class Float32Codec extends ScalarCodecBase<Float> {
    public Float32Codec() {
        super(Float.class);
    }

    @Override
    public void serialize(@NotNull PacketWriter writer, @Nullable Float value, CodecContext context) throws OperationNotSupportedException {
        if(value != null) {
            writer.write(value);
        }
    }

    @Override
    public @NotNull Float deserialize(@NotNull PacketReader reader, CodecContext context) {
        return reader.readFloat();
    }
}
