package com.aomsir.netty15.message;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MessageTypeToClass {
    public static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(MessageType.LOGIN_REQUEST_MESSAGE, LoginRequestMessage.class);
        messageClasses.put(MessageType.LOGIN_RESPONSE_MESSAGE, LoginResponseMessage.class);
        messageClasses.put(MessageType.CHAT_REQUEST_MESSAGE, ChatRequestMessage.class);
        messageClasses.put(MessageType.CHAT_RESPONSE_MESSAGE, ChatResponseMessage.class);
        messageClasses.put(MessageType.GROUP_CREATE_REQUEST_MESSAGE, GroupCreateRequestMessage.class);
        messageClasses.put(MessageType.GROUP_CREATE_RESPONSE_MESSAGE, GroupCreateResponseMessage.class);
        messageClasses.put(MessageType.GROUP_CHAT_REQUEST_MESSAGE, GroupChatRequestMessage.class);
        messageClasses.put(MessageType.GROUP_CHAT_RESPONSE_MESSAGE, GroupChatResponseMessage.class);
        messageClasses.put(MessageType.PING_MESSAGE, PPingMessage.class);

    }


}
