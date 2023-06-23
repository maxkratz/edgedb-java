package com.edgedb.driver.binary.packets.receivable;

import com.edgedb.driver.ErrorCode;
import com.edgedb.driver.ErrorSeverity;
import com.edgedb.driver.binary.PacketReader;
import com.edgedb.driver.binary.packets.ServerMessageType;
import com.edgedb.driver.binary.packets.shared.KeyValue;
import com.edgedb.driver.exceptions.EdgeDBErrorException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ErrorResponse implements Receivable {
    public final ErrorSeverity severity;
    public final ErrorCode errorCode;
    public final @NotNull String message;
    public final KeyValue @NotNull [] attributes;

    public ErrorResponse(@NotNull PacketReader reader) {
        severity = reader.readEnum(ErrorSeverity.class, Byte.TYPE);
        errorCode = reader.readEnum(ErrorCode.class, Integer.TYPE);
        message = reader.readString();
        attributes = reader.readAttributes();
    }

    public @NotNull EdgeDBErrorException toException() {
        return toException(null);
    }
    public @NotNull EdgeDBErrorException toException(@Nullable String query) {
        return new EdgeDBErrorException(
                Arrays.stream(attributes).collect(Collectors.toMap(v -> v.code, v -> {
                    if(v.value == null) {
                        return new byte[0];
                    }

                    var arr = new byte[v.value.readableBytes()];
                    v.value.readBytes(arr);
                    return arr;
                })),
                message,
                errorCode,
                query
        );
    }

    @Override
    public void close() throws Exception {
        release(attributes);
    }

    @Override
    public @NotNull ServerMessageType getMessageType() {
        return ServerMessageType.ERROR_RESPONSE;
    }
}
