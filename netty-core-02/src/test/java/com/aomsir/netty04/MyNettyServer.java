package com.aomsir.netty04;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @Author: Aomsir
 * @Date: 2022/10/26
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyNettyServer {

    private static final Logger log3 = LoggerFactory.getLogger(MyNettyServer.class);
    private static final Logger log2 = LoggerFactory.getLogger(MyNettyServer.class);
    private static final Logger log1 = LoggerFactory.getLogger(MyNettyServer.class);
    private static final Logger log = LoggerFactory.getLogger(MyNettyServer.class);

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);

        serverBootstrap.group(new NioEventLoopGroup());

        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            // 获取到一个read/write操作的channel
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("handler1", new ChannelInboundHandlerAdapter() {

                    // msg就是ByteBuf
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf byteBuf = (ByteBuf) msg;
                        String s = byteBuf.toString(Charset.defaultCharset());
                        log1.debug("handler1");
                        log2.debug("{}",s);

                        // 使参数在流水线中进行传输
                        super.channelRead(ctx, s);
                    }
                });

                pipeline.addLast("handler2", new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log1.debug("handler2");
                        log.debug("{}",msg);
                        super.channelRead(ctx, msg);
                    }
                });

                pipeline.addLast("handler3", new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log1.debug("handler3");
                        super.channelRead(ctx, msg);
                        channel.writeAndFlush("Hello,Aomsir!");
                    }
                });

                pipeline.addLast("handler4", new ChannelOutboundHandlerAdapter() {

                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        log3.debug("handler4");
                        super.write(ctx, msg, promise);
                    }
                });

                pipeline.addLast("handler5", new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        log3.debug("handler5");
                        super.write(ctx, msg, promise);
                    }
                });

                pipeline.addLast("handler6", new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        log3.debug("handler6");
                        super.write(ctx, msg, promise);
                    }
                });
            }
        });

        serverBootstrap.bind(8000);
    }
}
