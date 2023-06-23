package com.edgedb.driver.binary.descriptors.common;

import com.edgedb.driver.binary.packets.shared.Cardinality;
import com.edgedb.driver.binary.PacketReader;
import org.jetbrains.annotations.NotNull;
import org.joou.UInteger;
import org.joou.UShort;

import java.util.EnumSet;

public final class ShapeElement {
    public final @NotNull EnumSet<ShapeElementFlags> flags;
    public final Cardinality cardinality;
    public final @NotNull String name;
    public final @NotNull UShort typePosition;

    public ShapeElement(final @NotNull PacketReader reader) {
        this.flags = reader.readEnumSet(ShapeElementFlags.class, UInteger.class);
        this.cardinality = reader.readEnum(Cardinality.class, Byte.TYPE);
        this.name = reader.readString();
        this.typePosition = reader.readUInt16();
    }
}
