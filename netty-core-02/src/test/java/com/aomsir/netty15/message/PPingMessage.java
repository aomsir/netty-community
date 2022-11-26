package com.aomsir.netty15.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PPingMessage extends Message {

    private String source;

    @Override
    public int getMessageType() {
        return MessageType.PING_MESSAGE;
    }
}
