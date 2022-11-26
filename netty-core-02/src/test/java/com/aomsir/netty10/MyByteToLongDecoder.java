package com.aomsir.netty10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: Aomsir
 * @Date: 2022/11/10
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {

    private static final Logger log = LoggerFactory.getLogger(MyByteToLongDecoder.class);

    // ctx即当前pipeline相关的所有信息
    // msg代表client输出的信息
    // in代表client提交上来的数据
    // decode方法处理过程中,如果buffer没有处理完,那么他会重复调用decode方法
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.error("decode method invoke");
        // 因为一个long为8字节
        if (in.readableBytes() >= 8) {
            long receiveLong = in.readLong();
            out.add(receiveLong);
        }
        System.out.println(ByteBufUtil.prettyHexDump(in));
    }
}
