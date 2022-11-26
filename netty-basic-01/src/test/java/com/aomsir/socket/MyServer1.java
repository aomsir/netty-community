package com.aomsir.socket;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Aomsir
 * @Date: 2022/10/16
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyServer1 {
    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);   // 设置serverSocketChannel非阻塞,解决连接阻塞
        serverSocketChannel.bind(new InetSocketAddress(8000));

        List<SocketChannel> channelList = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.allocate(20);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();  // 不阻塞

            // 4、接受一个客户端就存一个
            if (socketChannel != null) {
                socketChannel.configureBlocking(false);  // 解决IO通信的阻塞,将接受的socketChannel设置IO非阻塞
                channelList.add(socketChannel);
            }

            // 5、client与服务端 通信 NIO
            for (SocketChannel channel : channelList) {
                int read = channel.read(buffer);// 阻塞,对于的IO通信的阻塞
                if (read > 0) {
                    System.out.println("开始实际的数据通信...");

                    buffer.flip();  // 开启读模式
                    CharBuffer result = StandardCharsets.UTF_8.decode(buffer);
                    System.out.println("result = " + result);
                    buffer.clear();  // 开启写模式

                    System.out.println("实际的通信已经结束...");
                }
            }
        }
    }
}
