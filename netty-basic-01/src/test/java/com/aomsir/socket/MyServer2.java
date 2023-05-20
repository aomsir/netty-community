package com.aomsir.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @Author: Aomsir
 * @Date: 2022/10/17
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyServer2 {
    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8000));
        serverSocketChannel.configureBlocking(false);    // Selector只在非阻塞下可用

        // 引入监管者
        Selector selector = Selector.open();

        // 让serverSocketChannel注册到监管者Selector上，并监控ServerSocketChannel的accept状态
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0, null);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);   // 监控accept

        // 监控
        while (true) {
            selector.select();  // 等待,只有监控到有实际的连接(ACCEPT)或读写操作才会处理。(实际的连接,监控到的)
                                // 监控到以后才会将ssc或者sc保存至 SelectionKeys(HashSet)里

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                // 用完以后,就将它从SelectionKeys中删除
                iterator.remove();

                if (key.isAcceptable()) {
                    // SSC
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);

                    // 监控sc状态   ---> keys
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);

                    System.out.println("accept = " + sc);
                } else if (key.isReadable()) {
                    try {
                        // SC - buffer -read/write
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(10);
                        int read = sc.read(buffer);

                        if (-1 == read) {
                            // 客户端处理完毕
                            key.channel();
                        } else {
                            buffer.flip();
                            System.out.println("Charset.defaultCharset().decode(buffer).toString() = " + Charset.defaultCharset().decode(buffer).toString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.channel();
                    }
                }
            }
        }
    }
}
