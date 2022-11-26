package com.aomsir.netty14;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @Author: Aomsir
 * @Date: 2022/10/26
 * @Description: ObjectDecoder/ObjectEncoder测试客户端
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyNettyClient {

    private static final Logger log = LoggerFactory.getLogger(MyNettyClient.class);

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        LoggingHandler loggingHandler = new LoggingHandler();
        StringEncoder stringEncoder = new StringEncoder();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(new NioEventLoopGroup());
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                //
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    channel.pipeline().addLast(loggingHandler);
                    channel.pipeline().addLast(stringEncoder);   // 编码器
                }
            });


            ChannelFuture future = bootstrap.connect(new InetSocketAddress(8000));
            future.sync();  // 阻塞main线程,等待上一步的连接真正的建立起来才放行
            Channel channel = future.channel();  // 获取与服务端的channel

            channel.writeAndFlush("hello,suns");

            channel.closeFuture().sync();   // 监控channel的关闭
        } catch (InterruptedException e) {
            log.error("client error {}", e.getMessage());
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
}
