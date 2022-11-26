package com.aomsir.netty02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Aomsir
 * @Date: 2022/10/26
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyNettyServer {

    private static final Logger log = LoggerFactory.getLogger(MyNettyServer.class);

    public static void main(String[] args) {
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(1);  // 1个boss
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup(3);  // 3个worker
        DefaultEventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);

        // select/read/write/accept 多线程的管理
        // 线程池 EventLoop -> worker
        // serverBootstrap.group(new NioEventLoopGroup());
        serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup);

        // 接收数据前/写数据前
        // 使用handler进行处理,一个核心处理
        // 通过pipeline来管理多个handler进行处理
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {

            // 获取到一个read/write操作的channel
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline().addLast(new StringDecoder());   // 解码操作
                channel.pipeline().addLast(defaultEventLoopGroup, new ChannelInboundHandlerAdapter() {
                    // 接口回调
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.debug("{}",msg);
                    }
                });  // 适配器

            }
        });

        serverBootstrap.bind(8000);
    }
}
