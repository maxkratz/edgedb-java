package com.edgedb.driver.binary.packets.receivable;

import com.edgedb.driver.binary.PacketReader;
import com.edgedb.driver.binary.packets.ServerMessageType;
import io.netty.buffer.ByteBuf;

public class ServerKeyData implements Receivable {
    public static final int SERVER_KEY_LENGTH = 32;

    public final ByteBuf keyData;

    public ServerKeyData(PacketReader reader) {
        keyData = reader.readBytes(SERVER_KEY_LENGTH);
    }

    @Override
    public ServerMessageType getMessageType() {
        return ServerMessageType.SERVER_KEY_DATA;
    }
}