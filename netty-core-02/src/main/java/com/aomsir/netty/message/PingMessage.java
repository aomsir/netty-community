package com.aomsir.netty.message;

import lombok.Data;

/**
 * ping请求Message
 */

@Data
public class PingMessage extends Message {

    private String source;

    public PingMessage() {
    }

    public PingMessage(String source) {
        this.source = source;
    }

    @Override
    public int getMessageType() {
        return MessageType.PING_MESSAGE;
    }
}
