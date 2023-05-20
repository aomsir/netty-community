package com.aomsir.netty.message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 聊天发送Message
 * 包含 从哪里发，发到哪里去，内容
 */

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
