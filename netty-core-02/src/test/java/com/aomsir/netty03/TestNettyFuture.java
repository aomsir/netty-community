package com.aomsir.netty03;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Aomsir
 * @Date: 2022/10/27
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNettyFuture {

    private static final Logger log1 = LoggerFactory.getLogger(TestNettyFuture.class);
    private static final Logger log = LoggerFactory.getLogger(TestNettyFuture.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        DefaultEventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup(2);

        EventLoop eventLoop = defaultEventLoopGroup.next();

        Future<Integer> future = eventLoop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                try {
                    log.debug("异步工作的处理...");
                    TimeUnit.SECONDS.sleep(10);
                    return 10;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return -1;   // future线程出现意外就返回-1,但不够
                }
            }
        });

//        log.debug("可以处理结果....");
//        log.debug("处理结果：{}",future.get());
//        log.debug("--------------------------");

        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                log.debug("异步处理的结果是{}",future.get());
            }
        });

        log.debug("-----------------");
    }
}
