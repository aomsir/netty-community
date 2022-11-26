package com.aomsir.netty07;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author: Aomsir
 * @Date: 2022/10/26
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyNettyServer1 {

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


                // 最大长度,最大长度内如果还未发现分隔符就不处理
                pipeline.addLast(new LineBasedFrameDecoder(10));
                pipeline.addLast(new LoggingHandler());
            }
        });

        serverBootstrap.bind(8000);
    }
}
