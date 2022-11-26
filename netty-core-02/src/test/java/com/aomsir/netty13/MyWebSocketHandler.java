package com.aomsir.netty13;

import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Aomsir
 * @Date: 2022/11/15
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger log = LoggerFactory.getLogger(MyWebSocketHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.error("接受到client的数据为{}", msg.text());

        Channel channel = ctx.channel();

        // 每两秒发一次
        // Timer timer = new Timer();
        // timer.schedule(new TimerTask() {
        // @Override
        // public void run() {
        // channel.writeAndFlush(new TextWebSocketFrame("Aomsir"));
        // }
        // }, 2000,2000);


        EventLoopGroup eventLoopGroup = new DefaultEventLoopGroup(2);
        EventLoop next = eventLoopGroup.next();
        next.schedule(() -> {
            channel.writeAndFlush(new TextWebSocketFrame("Aomsir"));  // 这里得封帧
        },2, TimeUnit.SECONDS);
    }
}
