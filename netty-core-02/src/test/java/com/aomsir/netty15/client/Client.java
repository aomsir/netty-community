package com.aomsir.netty15.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Client {

    private Bootstrap bootstrap = new Bootstrap();

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    private AtomicInteger retryTimes = new AtomicInteger(1);

    public Client() {
        bootstrap.channel(NioSocketChannel.class);
        Bootstrap group = bootstrap.group(eventLoopGroup);
        bootstrap.handler(new ChatChannelInitializer());
    }

    public void connect(String ip, int port) {
        ChannelFuture connect = bootstrap.connect(ip, port);
        connect.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    if (retryTimes.get() != 3) {
                        connect(ip, port);
                        retryTimes.getAndIncrement();
                    } else {
                        log.debug("server net error");
                    }
                } else {
                    Channel channel = connect.channel();
                    channel.closeFuture().addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (future.isSuccess()) {
                                eventLoopGroup.shutdownGracefully();
                            }
                        }
                    });
                }
            }
        });


    }
}
