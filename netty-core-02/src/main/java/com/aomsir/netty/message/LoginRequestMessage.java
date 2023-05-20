package com.aomsir.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 登录请求Message
 * 包含 用户名、密码
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequestMessage extends Message {
    private String username;
    private String password;

    @Override
    public int getMessageType() {
        return MessageType.LOGIN_REQUEST_MESSAGE;
    }
}
