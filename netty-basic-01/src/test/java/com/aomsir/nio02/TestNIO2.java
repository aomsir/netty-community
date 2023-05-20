package com.aomsir.nio02;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: Aomsir
 * @Date: 2022/10/15
 * @Description: Channel与Buffer读 - 优化案例 - 采用异常处理
 * @Email: info@say521.cn
 * @GitHub: <a href="https://github.com/aomsir">GitHub</a>
 */
public class TestNIO2 {
    public static void main(String[] args) {
        FileChannel channel = null;

        try {
            channel = new RandomAccessFile("/Users/aomsir/MyStudyProject/Java/Netty/netty-basic-01/data.txt","rw").getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(10);

            while (true) {
                int read = channel.read(buffer);
                if (-1 == read) {
                    break;
                }

                buffer.flip();

                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    System.out.println((char) b);
                }


                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }
        }
    }
}
