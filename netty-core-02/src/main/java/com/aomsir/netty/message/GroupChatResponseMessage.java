package com.aomsir.netty.message;

import lombok.Data;
import lombok.ToString;


/**
 * 群组消息响应Message
 * 包含 从哪里发、消息内容、状态码，原因
 */
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
