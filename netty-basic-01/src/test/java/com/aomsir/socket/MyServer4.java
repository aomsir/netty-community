package com.aomsir.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @Author: Aomsir
 * @Date: 2022/10/17
 * @Description: 缓冲区扩容
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyServer4 {
    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8000));
        serverSocketChannel.configureBlocking(false);    // Selector只在非阻塞下可用

        // 引入监管者
        Selector selector = Selector.open();

        // 让serverSocketChannel被selector管理
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0, null);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);   // 监控accept

        System.out.println("MyServer.main");

        // 监控
        while (true) {
            selector.select();  // 等待,只有监控到有实际的连接或读写操作才会处理。(实际的连接,监控到的)
                                // 监控到以后才会将ssc或者sc保存至 SelectionKeys(HashSet)里

            System.out.println("------------111-------------");

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();   // 用完以后,就将它从SelectionKeys中删除

                if (key.isAcceptable()) {
                    // ServerSocketChannel、获取的是最开始创建的
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);

                    // 监控sc状态   ---> keys
                    ByteBuffer buffer = ByteBuffer.allocate(7);
                    SelectionKey scKey = sc.register(selector, 0, buffer);  // 将buffer与channel绑定
                    scKey.interestOps(SelectionKey.OP_READ);

                    System.out.println("accept = " + sc);
                } else if (key.isReadable()) {
                    try {
                        // SC - buffer -read/write
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();   // 获取附件中的buffer
                        int read = sc.read(buffer);

                        if (-1 == read) {
                            // 客户端处理完毕
                            key.cancel();
                        } else {
                            doLineSplit(buffer);

                            // 没有压缩动,需要扩容
                            if (buffer.position() == buffer.limit()) {
                                // 1、空间扩大
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);

                                // 2、老缓冲区数据复制进新缓冲区
                                buffer.flip();
                                newBuffer.put(buffer);

                                // 3、绑定channel
                                // buffer = newBuffer;
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                }
            }
        }
    }

    private static void doLineSplit(ByteBuffer buffer) {
        buffer.flip(); // 读模式
        for (int i = 0; i < buffer.limit(); i++) {
            if (buffer.get(i) == '\n') {
                int length = i + 1 - buffer.position();  // 以免出现一行里面有多个\n
                ByteBuffer target = ByteBuffer.allocate(length);

                for (int j = 0; j < length; j++) {
                    target.put(buffer.get());
                }

                // 截取工作完成
                target.flip();
                System.out.println("StandardCharsets.UTF_8.decode(target) = " + StandardCharsets.UTF_8.decode(target));

                target.clear();
            }
        }

        buffer.compact(); // 写模式
    }
}
