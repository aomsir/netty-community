package com.aomsir.netty02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @Author: Aomsir
 * @Date: 2022/10/26
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyNettyClient {
    public static void main(String[] args) throws InterruptedException {
        // 1、统一编程模型

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            //
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline().addLast(new StringEncoder());  // 对发出的数据做编码
            }
        });

        // 连接会开辟一个独立的线程 这一步会交给新线程
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(8000));
        future.sync();   // 会阻塞,等待创建成功后的异步操作

        // 这是一个同步操作
        Channel channel = future.channel();  // 获取与服务端的channel
        channel.writeAndFlush("Aomsir");  // write数据,上面的eventLoop监听到就会调用对应的handler

    }
}
