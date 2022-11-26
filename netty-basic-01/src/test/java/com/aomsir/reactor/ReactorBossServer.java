package com.aomsir.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Aomsir
 * @Date: 2022/10/18
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class ReactorBossServer {

    private static final Logger log = LoggerFactory.getLogger(ReactorBossServer.class);

    public static void main(String[] args) throws Exception{
        log.debug("boss thread start...");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8000));

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        // 模拟线程池
        // Worker worker = new Worker("worker1");
        Worker[] workers = new Worker[2];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker - " + i);
        }

        AtomicInteger index = new AtomicInteger();

        while (true) {
            selector.select();

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey sscSelectionKey = iterator.next();
                iterator.remove();

                if (sscSelectionKey.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);

                    // sc.register(selector, SelectionKey.OP_READ);
                    log.debug("boss invoke worker register...");

                    // hash取模  x%2 = 0|1
                    workers[index.getAndIncrement() % workers.length].register(sc);
                    //worker.register(sc);
                    log.debug("boss invoke worker register...");
                }
            }
        }
    }
}
