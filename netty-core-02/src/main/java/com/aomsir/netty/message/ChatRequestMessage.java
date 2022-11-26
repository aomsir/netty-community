package com.aomsir.netty.message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ChatRequestMessage extends Message {

    private String from;
    private String to;
    private String content;

    @Override
    public int getMessageType() {
        return MessageType.CHAT_REQUEST_MESSAGE;
    }
}
