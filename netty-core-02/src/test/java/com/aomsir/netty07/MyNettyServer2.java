package com.aomsir.netty07;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

import java.io.Serializable;
import java.util.UUID;

/**
 * @Author: Aomsir
 * @Date: 2022/10/26
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyNettyServer2 {


    public static void main(String[] args) {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.group(new NioEventLoopGroup());

        // 接受socket缓冲区大小等同于设置了滑动窗口大小,初始值65535
        serverBootstrap.option(ChannelOption.SO_RCVBUF, 100);

        // netty接收client的数据,封装为ByteBuf,大小默认为1024
        // serverBootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16, 16, 16));  // 自定义ByteBuf
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            // 获取到一个read/write操作的channel
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();


                pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 1, 1));
                pipeline.addLast(new LoggingHandler());
            }
        });

        serverBootstrap.bind(8000);
    }
}
