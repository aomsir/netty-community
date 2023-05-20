package com.aomsir.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author: Aomsir
 * @Date: 2022/10/18
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class Worker implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Worker.class);
    private Selector selector;  // 自己用自己的selector,不建议暴露给Boss
    private Thread thread;
    private String name;

    // 通过volatile进行线程同步
    private volatile boolean isCreated;

    private ConcurrentLinkedQueue<Runnable> runnables = new ConcurrentLinkedQueue();

    public Worker(String name) {
        this.name = name;
    }

    // 线程任务
    public void register(SocketChannel sc) throws IOException,InterruptedException {
        log.debug("worker register invoke...");
        if (!isCreated) {
            thread = new Thread(this, name);
            thread.start();  // 调了start,不一定立马分配资源
            selector = Selector.open();
            isCreated = true;
        }
        // Thread.sleep(2000);  // 上面run()了,但没有监控ON_READ
        // System.out.println("register sleep...");
        // Thread.sleep(2000);  // 上面run()了,但没有监控ON_READ
        // System.out.println("register sleep...");
        // 有可能时间片在这里给了主线程

        runnables.add(() -> {
            try {
                sc.register(selector, SelectionKey.OP_READ);
            } catch (ClosedChannelException e) {
                throw new RuntimeException(e);
            }
        });
        // sc.register(selector, SelectionKey.OP_READ);  // 要先注册再阻塞,不然下面的阻塞了就注册不进来了,就死循环了

        selector.wakeup();    // 只要阻塞了就唤醒(以免下面阻塞着在) -运行之前注册能够唤醒,运行之后也能够唤醒
    }

    @Override
    public void run() {
        while (true) {
            log.debug("worker run method invoke...");
            try {
                selector.select();

                Runnable poll = runnables.poll();
                if (poll != null) {
                    poll.run();
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey scKey = iterator.next();
                    iterator.remove();

                    if (scKey.isReadable()) {
                        SocketChannel sc = (SocketChannel) scKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(30);

                        int read = sc.read(buffer);
                        if (read == -1) {
                            scKey.cancel();
                            break;
                        }
                        buffer.flip();
                        String result = Charset.defaultCharset().decode(buffer).toString();
                        System.out.println("result = " + result);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
