package com.aomsir.netty.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class ChatResponseMessage extends AbstractResponseMessage {
    private String from;
    private String content;

    public ChatResponseMessage(String code, String resaon) {
        super(code, resaon);
    }

    public ChatResponseMessage(String code, String rsaon, String from, String content) {
        this(code, rsaon);
        this.from = from;
        this.content = content;
    }

    public ChatResponseMessage() {
    }

    @Override
    public int getMessageType() {
        return MessageType.CHAT_RESPONSE_MESSAGE;
    }
}
