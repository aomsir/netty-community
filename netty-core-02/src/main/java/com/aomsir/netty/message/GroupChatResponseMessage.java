package com.aomsir.netty.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupChatResponseMessage extends AbstractResponseMessage {
    private String from;
    private String content;

    public GroupChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }

    public GroupChatResponseMessage() {
    }

    @Override
    public int getMessageType() {
        return MessageType.GROUP_CHAT_RESPONSE_MESSAGE;
    }
}
