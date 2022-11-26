package com.aomsir.netty02;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Aomsir
 * @Date: 2022/10/25
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestEventLoopGroup {

    private static final Logger log = LoggerFactory.getLogger(TestEventLoopGroup.class);

    public static void main(String[] args) {
        // 创建多个EventLoop(线程),并保存起来
        // 参数指定线程个数,可以空参(创建CPU对应线程数)
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);
        EventLoop el1 = eventLoopGroup.next();
        EventLoop el2 = eventLoopGroup.next();
        EventLoop el3 = eventLoopGroup.next();

        System.out.println("el1 = " + el1);  // el1 = io.netty.channel.nio.NioEventLoop@7d4793a8
        System.out.println("el2 = " + el2);  // el2 = io.netty.channel.nio.NioEventLoop@449b2d27
        System.out.println("el3 = " + el3);  // el1 = io.netty.channel.nio.NioEventLoop@7d4793a8

        System.out.println("NettyRuntime.availableProcessors() = " + NettyRuntime.availableProcessors());  // NioEventLoop空参构造方法的线程数

        DefaultEventLoopGroup defaultEventLoopGroups = new DefaultEventLoopGroup();
        EventLoop defaultEventLoop = defaultEventLoopGroups.next();

        defaultEventLoop.submit(() -> {
            log.debug("hello");
        });
    }
}
