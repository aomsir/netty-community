package com.aomsir;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @Author: Aomsir
 * @Date: 2022/12/4
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestEventLoop2 {

    public static void main(String[] args) {
        EventLoop eventLoop = new NioEventLoopGroup().next();

        eventLoop.execute(() -> {
            System.out.println("hello, Aomsir");
        });
    }
}
