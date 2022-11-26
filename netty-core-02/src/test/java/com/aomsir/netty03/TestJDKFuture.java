package com.aomsir.netty03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @Author: Aomsir
 * @Date: 2022/10/27
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestJDKFuture {

    private static final Logger log = LoggerFactory.getLogger(TestJDKFuture.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /**
         * JDK中考虑异步化的才会考虑Future
         * 启动一个新线程,执行完以后将处理结果返回给调用线程
         */
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<Integer> future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("异步工作的处理...");
                TimeUnit.SECONDS.sleep(10);
                return 10;
            }
        });

        log.debug("可以处理结果....");
        log.debug("处理结果：{}",future.get());
        log.debug("--------------------------");
    }
}


// 执行结果
// 可以处理结果....
// 异步工作的处理...
// 处理结果：10
// --------------------------
