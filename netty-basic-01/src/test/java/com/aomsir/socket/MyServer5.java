package com.aomsir.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @Author: Aomsir
 * @Date: 2022/10/17
 * @Description: 服务器的写操作
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyServer5 {
    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8000));


        Selector selector = Selector.open();
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0, null);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);


        while (true) {
            selector.select();  // 阻塞,等待新的连接

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey sscKey = iterator.next();
                iterator.remove();

                if (sscKey.isAcceptable()) {
                    SocketChannel sc = serverSocketChannel.accept();
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);


                    // 准备数据
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0;i < 2000000;i++) {
                        sb.append("aomsirilosjsfddddddddddddddd");
                    }

                    // NIO Buffer存储数据,Channel发送数据
                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                    int write = sc.write(buffer);
                    System.out.println("write = " + write);

                    if (buffer.hasRemaining()) {
                        // 为当前的SocketChannel增加 Write监听
                        // READ + Write
                        scKey.interestOps(scKey.interestOps() + SelectionKey.OP_WRITE);
                        scKey.attach(buffer);  // 将剩余数据处理的buffer传递过去
                    }

                } else if (sscKey.isWritable()) {
                    SocketChannel sc = (SocketChannel) sscKey.channel();
                    ByteBuffer buffer = (ByteBuffer) sscKey.attachment();

                    int write = sc.write(buffer);
                    System.out.println("write = " + write);

                    if (!buffer.hasRemaining()) {
                        sscKey.attach(null);
                        sscKey.interestOps(sscKey.interestOps() - SelectionKey.OP_WRITE);
                    }
                }
            }
        }
    }
}
