package com.aomsir.netty.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class LoginResponseMessage extends AbstractResponseMessage {

    public LoginResponseMessage(String code, String resaon) {
        super(code, resaon);
    }

    public LoginResponseMessage() {
    }

    @Override
    public int getMessageType() {
        return MessageType.LOGIN_RESPONSE_MESSAGE;
    }
}
