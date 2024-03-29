package com.aomsir.netty.codec;

import com.aomsir.netty.message.Message;
import com.aomsir.netty.message.MessageTypeToClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 解码器
 */

@Slf4j
public class ChatByteToMessageDecoder extends ByteToMessageDecoder{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
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

        //4. 功能指令(就是消息类型)
        byte funcNo = in.readByte();
        log.debug("功能指令 {} ",funcNo);

        //5. 正文长度
        int contentLength = in.readInt();
        log.debug("正文长度 {}",contentLength);

        //6.根据功能指令获取正文
        Message message = null;
        if(serializableNo == 1){
            ObjectMapper objectMapper = new ObjectMapper();

            // 将ByteBuf中的内容取出来，反序列化成Message类型
            message = objectMapper.readValue(in.readCharSequence(contentLength,Charset.defaultCharset()).toString(),
                    MessageTypeToClass.messageClasses.get((int)funcNo));
        }
        out.add(message);
    }
}
