package com.aomsir.netty15.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class ChatRequestMessage extends Message {
    private String content;
    private String to;
    private String from;

    @Override
    public int getMessageType() {
        return MessageType.CHAT_REQUEST_MESSAGE;
    }
}
