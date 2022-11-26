package com.aomsir.netty15;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.aomsir.netty15.message.Message;
import com.aomsir.netty15.message.MessageTypeToClass;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

@ChannelHandler.Sharable
@Slf4j
public class MyByteToMessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
        log.debug("decode method invoke....");
        //1. 幻数  4个字节
        ByteBuf byteBuf = in.readBytes(4);
        log.debug("幻数是 {}",byteBuf.toString(Charset.defaultCharset()));

        //2. 协议版本 1个字节
        byte protoVersion = in.readByte();
        log.debug("协议版本是 {} ",protoVersion);

        //3. 序列化方式
        byte serializableNo = in.readByte();
        log.debug("序列化方式 {} ",serializableNo);

        //4. 功能指令
        byte funcNo = in.readByte();
        log.debug("功能指令 {} ",funcNo);

        //5. 正文长度
        int contentLength = in.readInt();
        log.debug("正文长度 {}",contentLength);

        //6.正文
        Message message = null;
        if(serializableNo == 1){
            ObjectMapper objectMapper = new ObjectMapper();
            //message = objectMapper.readValue(in.toString(Charset.defaultCharset()), Message.class);
            Class<? extends Message> messageClass = MessageTypeToClass.messageClasses.get((int)funcNo);
            message = objectMapper.readValue(in.readCharSequence(contentLength,Charset.defaultCharset()).toString(),messageClass);
        }
        out.add(message);
    }
}
