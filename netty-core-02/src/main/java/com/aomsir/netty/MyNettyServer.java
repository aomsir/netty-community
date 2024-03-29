package com.aomsir.netty;

import com.aomsir.netty.codec.ChatByteToMessageDecoder;
import com.aomsir.netty.codec.ChatMessageToByteEncoder;
import com.aomsir.netty.handler.*;
import com.aomsir.netty.message.PongMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j
public class MyNettyServer {

    public static void main(String[] args) {
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();           // 日志处理器
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);    // boss线程 1个
        EventLoopGroup workerGroup = new NioEventLoopGroup();            // worker线程 2*CPU核心数

        // 自定义消息handler
        LoginRequestMessageHandler LOGIN_REQUEST_MESSAGE_HANDLER = new LoginRequestMessageHandler();
        ChatRequestMessageHandler CHAT_REQUEST_MESSAGE_HANDLER = new ChatRequestMessageHandler();
        GroupChatRequestMessageHandler GROUP_CHAT_REQUEST_MESSAGE_HANDLER = new GroupChatRequestMessageHandler();
        GroupCreateMessageHandler GROUP_CREATE_MESSAGE_HANDLER = new GroupCreateMessageHandler();
        QuitHandler QUITHANDLER = new QuitHandler();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(bossGroup, workerGroup);

            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    // 解决封帧
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 7, 4, 0, 0));
                    pipeline.addLast(LOGGING_HANDLER);

                    // 编解码器
                    pipeline.addLast(new ChatByteToMessageDecoder());
                    pipeline.addLast(new ChatMessageToByteEncoder());

                    // 读写空闲监控
                    pipeline.addLast(new IdleStateHandler(8,3,0, TimeUnit.SECONDS));
                    pipeline.addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
                            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                                log.error("client已经8秒没与服务端通信...");
                                ctx.channel().close();
                                log.error("关闭channel");
                            } else if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                                ctx.writeAndFlush(new PongMessage("server"));
                            }
                        }
                    }).addLast(LOGIN_REQUEST_MESSAGE_HANDLER);

                    //消息处理器
                    pipeline.addLast(CHAT_REQUEST_MESSAGE_HANDLER);
                    pipeline.addLast(GROUP_CHAT_REQUEST_MESSAGE_HANDLER);
                    pipeline.addLast(GROUP_CREATE_MESSAGE_HANDLER);
                    pipeline.addLast(QUITHANDLER);

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
