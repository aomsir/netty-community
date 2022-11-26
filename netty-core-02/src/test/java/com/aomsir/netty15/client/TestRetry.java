package com.aomsir.netty15.client;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestRetry {
    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        EventLoop next = eventLoopGroup.next();

        /*
        next.schedule(() -> {
            log.debug("connection...");
        }, 3, TimeUnit.SECONDS);
        */

        next.scheduleAtFixedRate(() -> {
            log.debug("connection...");
        }, 3, 3, TimeUnit.SECONDS);


    }
}
