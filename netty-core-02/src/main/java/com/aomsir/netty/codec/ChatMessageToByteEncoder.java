package com.aomsir.netty.codec;

import com.aomsir.netty.message.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class ChatMessageToByteEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        log.debug("encode method invoke ");
        //1. 幻术 4个字节 suns
        out.writeBytes(new byte[]{'s', 'u', 'n', 's'});
        //2. 协议版本 1个字节
        out.writeByte(1);
        //3. 序列化方式 1个字节 1.json 2 protobuf 3 hession
        out.writeByte(1);

        //4. 根据消息获取功能指令
        out.writeByte(msg.getMessageType());

        // 将消息封装成json
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(msg);

        //5. 正文长度 4个字节
        out.writeInt(jsonContent.length());

        //6. 正文
        out.writeCharSequence(jsonContent, Charset.defaultCharset());

    }
}
