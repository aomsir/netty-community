package com.aomsir.socket;

import java.net.InetSocketAddress;
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
 * @Description: 服务器端,使用ServerSocketChannel
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyServer {
    public static void main(String[] args) throws Exception{
        // 1、创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 2、设置服务端的监听端口
        serverSocketChannel.bind(new InetSocketAddress(8000));

        List<SocketChannel> channelList = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.allocate(20);

        // 3、接受客户端的连接,让它一直转
        while (true) {
            // 4、SocketChannel代表服务端与Client链接的一个通道
            System.out.println("等待连接服务器...");
            SocketChannel socketChannel = serverSocketChannel.accept();  // 发生阻塞,程序在等待客户端的请求
            System.out.println("服务器已连接..." + socketChannel);

            // 4-1、接受一个客户端就存一个
            channelList.add(socketChannel);

            // 5、client与服务端 通信 NIO
            for (SocketChannel channel : channelList) {
                System.out.println("开始实际的数据通信...");

                channel.read(buffer);   // 阻塞,对于的IO通信的阻塞
                buffer.flip();  // 开启读模式
                CharBuffer result = StandardCharsets.UTF_8.decode(buffer);
                System.out.println("result = " + result);
                buffer.clear();  // 开启写模式

                System.out.println("实际的通信已经结束...");
            }
        }
    }
}
