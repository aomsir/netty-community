package com.aomsir.netty15;

import com.aomsir.netty15.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyNettyServer {

    private static final Logger log = LoggerFactory.getLogger(MyNettyServer.class);

    public static void main(String[] args) {
        LoggingHandler LOGGING_GHANDLER = new LoggingHandler();
        MyMessageToByteEncoder myMessageToByteEncoder = new MyMessageToByteEncoder();
        MyByteToMessageDecoder myByteToMessageDecoder = new MyByteToMessageDecoder();
        LoginRequestMessageHandler LOGIN_REQUEST_MESSAGE_HANDLER = new LoginRequestMessageHandler();
        ChatRequestMessageHandler CHATREQUESTMESSAGEHANDLER = new ChatRequestMessageHandler();

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 7, 4, 0, 0));
                    ch.pipeline().addLast(myByteToMessageDecoder);
                    ch.pipeline().addLast(myMessageToByteEncoder);
                    pipeline.addLast(LOGGING_GHANDLER);
                    pipeline.addLast(new IdleStateHandler(8, 0, 0));
                   /* pipeline.addLast(new ChannelDuplexHandler() {
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
                            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                                //log.debug("已经 8秒 没有读取到数据了..");
                                //ctx.channel().close();
                            }
                        }
                    });*/
                    pipeline.addLast(LOGIN_REQUEST_MESSAGE_HANDLER);
                    pipeline.addLast(CHATREQUESTMESSAGEHANDLER);
                }
            });
            Channel channel = serverBootstrap.bind(8000).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("server error", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
