package com.edgedb.driver.binary.packets.receivable;

import com.edgedb.driver.Capabilities;
import com.edgedb.driver.binary.packets.shared.Annotation;
import com.edgedb.driver.binary.PacketReader;
import com.edgedb.driver.binary.packets.ServerMessageType;
import com.edgedb.driver.binary.packets.shared.Cardinality;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.UUID;

public class CommandDataDescription implements Receivable {
    public final Annotation[] annotations;
    public final EnumSet<Capabilities> capabilities;
    public final Cardinality cardinality;
    public final UUID inputTypeDescriptorId;
    public final UUID outputTypeDescriptorId;
    public final @Nullable ByteBuf inputTypeDescriptorBuffer;
    public final @Nullable ByteBuf outputTypeDescriptorBuffer;

    public CommandDataDescription(PacketReader reader) {
        annotations = reader.readAnnotations();
        capabilities = reader.readEnumSet(Capabilities.class, Long.TYPE);
        cardinality = reader.readEnum(Cardinality.class, Byte.TYPE);
        inputTypeDescriptorId = reader.readUUID();
        inputTypeDescriptorBuffer = reader.readByteArray();
        outputTypeDescriptorId = reader.readUUID();
        outputTypeDescriptorBuffer = reader.readByteArray();
    }

    @Override
    public ServerMessageType getMessageType() {
        return ServerMessageType.COMMAND_DATA_DESCRIPTION;
    }
}