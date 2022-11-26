package com.aomsir.netty09;

import com.sun.deploy.net.HttpRequest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Aomsir
 * @Date: 2022/11/8
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class NettyHttpServer2 {

    private static final Logger log2 = LoggerFactory.getLogger(NettyHttpServer2.class);
    private static final Logger log1 = LoggerFactory.getLogger(NettyHttpServer2.class);
    private static final Logger log = LoggerFactory.getLogger(NettyHttpServer2.class);

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.group(new NioEventLoopGroup());


        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast(new LoggingHandler());
                pipeline.addLast(new HttpServerCodec());  // 与http有关的编解码器,接受请求时解码,接受响应时编码
            }
        });

        serverBootstrap.bind(8000);
    }
}
