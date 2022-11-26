package com.aomsir.netty.message;

/**
 * @Author: Aomsir
 * @Date: 2022/11/26
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class PongMessage extends AbstractResponseMessage{
    private String source;

    public PongMessage(String source) {
        this.source = source;
    }

    public PongMessage() {

    }

    @Override
    public int getMessageType() {
        return MessageType.PONG_MESSAGE;
    }
}
