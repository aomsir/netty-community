package com.aomsir.netty14.sharable;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author: Aomsir
 * @Date: 2022/11/10
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyByteToMessageDecoder extends ByteToMessageDecoder {
    private static final Logger log = LoggerFactory.getLogger(MyByteToMessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.debug("decode message invoke");

        // 1、幻术,4个字节、suns
        ByteBuf byteBuf = in.readBytes(4);
        log.debug("幻数是 {}", byteBuf.toString(Charset.defaultCharset()));

        // 2、协议版本、1个字节
        byte protoVersion = in.readByte();
        log.debug("协议版本是 {}", protoVersion);


        // 3、序列化方式、1个字节 1-json、2-protobuf、3-hession
        byte serializableNo = in.readByte();
        log.debug("序列化方式是 {}", serializableNo);


        // 4、功能指令、1-登陆 2-注册
        byte funcNo = in.readByte();
        log.debug("功能指令是 {}", serializableNo);


        // 5、正文长度、4字节
        int contentLength = in.readInt();
        log.debug("正文长度是 {}", contentLength);


        // 6、正文
        Message message = null;
        if (serializableNo == 1) {
            ObjectMapper objectMapper = new ObjectMapper();
            // message = objectMapper.readValue(in.toString(Charset.defaultCharset()), Message.class);  // 一定要注意这里
            message = objectMapper.readValue(in.readCharSequence(contentLength, Charset.defaultCharset()).toString(), Message.class);
        }
        out.add(message);
    }
}
