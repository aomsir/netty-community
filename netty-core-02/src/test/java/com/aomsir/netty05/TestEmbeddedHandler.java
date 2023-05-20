package com.aomsir.netty05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Aomsir
 * @Date: 2022/10/30
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestEmbeddedHandler {
    public static void main(String[] args) {
        final Logger log = LoggerFactory.getLogger(TestEmbeddedHandler.class);

        ChannelInboundHandlerAdapter h1 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("h1,{}", msg);
                super.channelRead(ctx, msg);
            }
        };

        ChannelInboundHandlerAdapter h2 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("h2,{}",msg);
                super.channelRead(ctx, msg);
            }
        };

        ChannelInboundHandlerAdapter h3 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("h3,{}",msg);
                super.channelRead(ctx, msg);
            }
        };

        ChannelOutboundHandlerAdapter h4 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.debug("h4");
                super.write(ctx, msg, promise);
            }
        };

        ChannelOutboundHandlerAdapter h5 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.debug("h5");
                super.write(ctx, msg, promise);
            }
        };

        ChannelOutboundHandlerAdapter h6 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.debug("h6");
                super.write(ctx, msg, promise);
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(h1, h2, h3, h4, h5, h6);
        channel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("Aomsir".getBytes()));   // 测试Handler使用这种方式
        channel.writeOutbound("Outbound");
    }
}
