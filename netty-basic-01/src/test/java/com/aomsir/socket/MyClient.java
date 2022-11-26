package com.aomsir.socket;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @Author: Aomsir
 * @Date: 2022/10/16
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyClient {
    public static void main(String[] args) throws Exception{
        // 1、创建客户端channel,并连接服务端
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8000));
        socketChannel.write(Charset.defaultCharset().encode("你好，我是黄祥河\n"));
        System.out.println("-------------------------------------");

        // 2、
    }
}
