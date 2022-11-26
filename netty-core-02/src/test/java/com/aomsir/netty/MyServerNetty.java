package com.aomsir.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Author: Aomsir
 * @Date: 2022/10/22
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyServerNetty {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.channel(NioServerSocketChannel.class);

        // 创建了一组线程,死循环监控状态(accept/read/write)
        // 线程池 EventLoop --> worker
        serverBootstrap.group(new NioEventLoopGroup());

        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            // 为管道做初始化
            // channel 接通监控，处理流水线，用handler进行处理
            //  下面写多少个handler取决与业务
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                // ByteBuff -> 字符(解码)
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        // msg为流水线上一步操作解码的字符串,也可以不用解码,自己手动解码
                        System.out.println("msg = " + msg);
                    }
                });
            }
        });

        serverBootstrap.bind(8000);
    }
}
