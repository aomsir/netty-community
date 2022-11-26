package com.aomsir.netty.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupCreateResponseMessage extends AbstractResponseMessage {

    public GroupCreateResponseMessage() {
    }

    public GroupCreateResponseMessage(String code, String reason) {
        super(code, reason);
    }

    @Override
    public int getMessageType() {
        return MessageType.GROUP_CREATE_RESPONSE_MESSAGE;
    }
}
