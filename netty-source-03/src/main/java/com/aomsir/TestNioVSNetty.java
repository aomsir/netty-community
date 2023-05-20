package com.aomsir;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @Author: Aomsir
 * @Date: 2022/11/30
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNioVSNetty {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 1.NIO服务端开发步骤
        Selector selector = Selector.open();   // 选择器

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();  // ServerSocketChannel
        serverSocketChannel.configureBlocking(false);  // 设置非阻塞

        SelectionKey selectionKey = serverSocketChannel.register(selector, 0, null);  // selector进行绑定注册

        selectionKey.interestOps(SelectionKey.OP_ACCEPT);   // 监听请求
        serverSocketChannel.bind(new InetSocketAddress(8000));   // 绑定端口


        // 2.Netty服务端开发步骤
        ServerBootstrap serverBootstrap = new ServerBootstrap();  // 创建ServerBootstrap
        serverBootstrap.channel(NioServerSocketChannel.class);    // 设置NioServerSocketChannel
        serverBootstrap.group(new NioEventLoopGroup());           // 指定group、此时不区分worker

        // 最核心,添加childHandler
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline().addLast(new LoggingHandler());
            }
        });

        // 绑定端口,阻塞
        Channel channel = serverBootstrap.bind(new InetSocketAddress(8000)).sync().channel();
        channel.closeFuture().sync();
    }
}
