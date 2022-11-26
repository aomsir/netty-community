package com.aomsir.nio02;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: Aomsir
 * @Date: 2022/10/15
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNIO1 {
    public static void main(String[] args) throws IOException {
        // 1.创建Channel通道 - FileChannel
        FileChannel channel = new FileInputStream("/Users/aomsir/MyStudyProject/Java/Netty/netty-basic-01/data.txt").getChannel();

        // 2.创建Buffer缓冲区,容量为10字节,具体根据文件编码来定
        ByteBuffer buffer = ByteBuffer.allocate(10);

        while (true) {
            // 3.把channel读取的数据放入buffer,读完以后返回的是-1
            int read = channel.read(buffer);
            if (-1 == read) {
                break;
            }

            // 4.设置buffer为读模式
            buffer.flip();

            // 5.循环读取缓冲区数据
            while (buffer.hasRemaining()) {
                byte b = buffer.get();
                System.out.println((char) b);
            }

            // 6.操作之后将buffer设置为写模式
            buffer.clear();
        }

    }
}
