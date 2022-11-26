package com.aomsir.netty;

import com.aomsir.netty.codec.ChatByteToMessageDecoder;
import com.aomsir.netty.codec.ChatMessageToByteEncoder;
import com.aomsir.netty.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MyNettyServer {

    public static void main(String[] args) {
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 自定义消息handler
        LoginRequestMessageHandler LOGINREQUESTMESSAGEHANDLER = new LoginRequestMessageHandler();
        ChatRequestMessageHandler CHATREQUESTMESSAGEHANDLER = new ChatRequestMessageHandler();
        GroupChatRequestMessageHandler GROUPCHATREQUESTMESSAGEHANDLER = new GroupChatRequestMessageHandler();
        GroupCreateMessageHandler GROUPCREATEMESSAGEHANDLER = new GroupCreateMessageHandler();
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

                    //消息处理器
                    pipeline.addLast(LOGINREQUESTMESSAGEHANDLER);
                    pipeline.addLast(CHATREQUESTMESSAGEHANDLER);
                    pipeline.addLast(GROUPCHATREQUESTMESSAGEHANDLER);
                    pipeline.addLast(GROUPCREATEMESSAGEHANDLER);
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
