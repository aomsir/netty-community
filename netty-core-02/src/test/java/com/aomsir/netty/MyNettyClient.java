package com.aomsir.netty;

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
 * @Date: 2022/10/22
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyNettyClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);


        // 为什么客户端也要引入循环组
        // 因为客户端也做的是多线程,异步
        // 连接和通信做成两个线程
        bootstrap.group(new NioEventLoopGroup());

        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringEncoder());
            }
        });

        // connect开辟了一个新的线程用于连接
        ChannelFuture connect = bootstrap.connect(new InetSocketAddress(8000));

        connect.sync();   //阻塞线程,只有连上才会有后续的异步
        Channel channel = connect.channel();  // 连上以后再去读写
        channel.writeAndFlush("Hello Aomsir");

    }
}
