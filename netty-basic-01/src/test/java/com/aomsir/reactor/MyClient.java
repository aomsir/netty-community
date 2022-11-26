package com.aomsir.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger log = LoggerFactory.getLogger(MyClient.class);

    public static void main(String[] args) throws Exception{
        // 1、创建客户端channel,并连接服务端
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8000));
        socketChannel.write(Charset.defaultCharset().encode("hello\n"));
        System.out.println("-------------------------------------");

        // 2、

    }
}
