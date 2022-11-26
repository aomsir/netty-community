package com.aomsir.netty13;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
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
public class MyNettyServer {

    private static final Logger log = LoggerFactory.getLogger(MyNettyServer.class);

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.group(new NioEventLoopGroup());

        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast(new StringDecoder());

                // 读空闲时间、写空闲时间、读写空间时间、时间单位
                pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                pipeline.addLast(new ChannelInboundHandlerAdapter() {
                    // 当发生了特定的空闲时间以后,程序员的处理业务写在这里
                    @Override
                    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                        IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
                        if (idleStateEvent.state() == IdleState.READER_IDLE) {
                            log.error("读空闲-{}", ctx.channel());
                        } else if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                            log.error("写空闲-{}", ctx.channel());
                        } else if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                            log.error("读写空闲-{}", ctx.channel());
                        }
                        super.userEventTriggered(ctx, evt);
                    }
                });
            }
        });

        serverBootstrap.bind(8000);
    }
}
