package com.aomsir.netty03;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Aomsir
 * @Date: 2022/10/27
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestPromise {

    private static final Logger log = LoggerFactory.getLogger(TestPromise.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoop eventLoop = new DefaultEventLoop().next();

        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(()-> {
            log.debug("异步处理开始...");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }

            promise.setSuccess(10);    // 使用promise设置返回值
        }).start();

        log.debug("等待异步处理的结果...");
        log.debug("结果是{}",promise.get());
        log.debug("---------------");
    }
}
