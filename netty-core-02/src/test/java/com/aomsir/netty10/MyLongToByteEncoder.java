package com.aomsir.netty10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Aomsir
 * @Date: 2022/11/10
 * @Description: 自定义编码器
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<String> {

    private static final Logger log = LoggerFactory.getLogger(MyLongToByteEncoder.class);

    // ctx即当前pipeline相关的所有信息
    // msg代表client输出的信息
    // out代表真正往服务端写的ByteBuf的数据
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        log.error("encode method invoke");
        String[] messages = msg.split("-");
        for (String message : messages) {
            long resultLong = Long.parseLong(message);
            out.writeLong(resultLong);
        }
    }
}
