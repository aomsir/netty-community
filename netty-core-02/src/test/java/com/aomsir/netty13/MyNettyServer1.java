package com.aomsir.netty13;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Aomsir
 * @Date: 2022/10/26
 * @Description: ObjectDecoder/ObjectEncoder测试服务端
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyNettyServer1 {

    private static final Logger log = LoggerFactory.getLogger(MyNettyServer1.class);

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.group(new NioEventLoopGroup());

        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast(new LoggingHandler());
                pipeline.addLast(new HttpServerCodec());  // WebSocket是支持在Http协议之上的
                pipeline.addLast(new HttpObjectAggregator(1024));   // 转换为FullRequest

                // 协议：ws://xxxx:8000/aomsir
                pipeline.addLast(new WebSocketServerProtocolHandler("/aomsir"));

                pipeline.addLast(new MyWebSocketHandler());  // 自定义handler
            }
        });

        serverBootstrap.bind(8000);
    }
}
