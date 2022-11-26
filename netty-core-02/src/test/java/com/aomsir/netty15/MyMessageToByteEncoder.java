package com.aomsir.netty15;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.aomsir.netty15.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

@ChannelHandler.Sharable
@Slf4j
public class MyMessageToByteEncoder extends MessageToMessageEncoder<Message> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        log.debug("encode method invoke ");

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        //1. 幻术 4个字节 suns
        byteBuf.writeBytes(new byte[]{'s', 'u', 'n', 's'});
        //2. 协议版本 1个字节
        byteBuf.writeByte(1);
        //3. 序列化方式 1个字节 1.json 2 protobuf 3 hession
        byteBuf.writeByte(1);
        //4. 功能指令 1个字节  1 登录 2 注册
        byteBuf.writeByte(msg.getMessageType());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(msg);

        //5. 正文长度 4个字节
        byteBuf.writeInt(jsonContent.length());

        //6. 正文
        byteBuf.writeCharSequence(jsonContent, Charset.defaultCharset());

        out.add(byteBuf);
    }
}
