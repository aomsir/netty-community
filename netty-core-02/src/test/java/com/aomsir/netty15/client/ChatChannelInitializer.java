package com.aomsir.netty15.client;

import com.aomsir.netty15.MyByteToMessageDecoder;
import com.aomsir.netty15.MyMessageToByteEncoder;
import com.aomsir.netty15.clienthandler.UIHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    LoggingHandler LOGGING_HANDLER = new LoggingHandler();
    MyMessageToByteEncoder myMessageToByteEncoder = new MyMessageToByteEncoder();
    MyByteToMessageDecoder myByteToMessageDecoder = new MyByteToMessageDecoder();

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 7, 4, 0, 0));
        ch.pipeline().addLast("logging", LOGGING_HANDLER);
        ch.pipeline().addLast(myByteToMessageDecoder);
        ch.pipeline().addLast(myMessageToByteEncoder);
        //ch.pipeline().addLast(new IdleStateHandler(0, 5, 0));
        ch.pipeline().addLast(new IdleStateHandler(8, 0, 0));
        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
                if (idleStateEvent.state() == IdleState.READER_IDLE) {
                    log.debug("空闲8秒关闭连接...");
                    ctx.channel().close();
                }
            }
        });
        ch.pipeline().addLast(new UIHandler());
    }
}
