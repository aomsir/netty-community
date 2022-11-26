package com.aomsir.netty10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: Aomsir
 * @Date: 2022/11/10
 * @Description: 自定义编解码器,组合类型
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyLongCodec extends ByteToMessageCodec {

    private static final Logger log = LoggerFactory.getLogger(MyLongCodec.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        log.error("encode method invoke");
        String[] messages = ((String) msg).split("-");
        for (String message : messages) {
            long resultLong = Long.parseLong(message);
            out.writeLong(resultLong);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List out) throws Exception {
        log.error("decode method invoke");
        // 因为一个long为8字节
        if (in.readableBytes() >= 8) {
            long receiveLong = in.readLong();
            out.add(receiveLong);
        }
        System.out.println(ByteBufUtil.prettyHexDump(in));
    }
}
