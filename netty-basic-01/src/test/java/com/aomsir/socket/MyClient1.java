package com.aomsir.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author: Aomsir
 * @Date: 2022/10/17
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyClient1 {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8000));


        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        // 读取服务端数据
        int read = 0;
        while (true) {
            read += socketChannel.read(buffer);
            System.out.println("read = " + read);
            buffer.clear();
        }
    }
}
