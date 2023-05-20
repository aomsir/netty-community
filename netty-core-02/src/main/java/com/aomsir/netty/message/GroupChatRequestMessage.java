package com.aomsir.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群组消息发送Message
 * 消息从哪里发送，群组名，消息内容
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatRequestMessage extends Message{
    private String from;
    private String groupName;
    private String content;

    @Override
    public int getMessageType() {
        return MessageType.GROUP_CHAT_REQUEST_MESSAGE;
    }
}
