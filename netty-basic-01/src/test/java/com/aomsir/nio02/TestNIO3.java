package com.aomsir.nio02;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author: Aomsir
 * @Date: 2022/10/15
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNIO3 {
    public static void main(String[] args) {
        FileChannel channel = null;

        try {
            channel = FileChannel.open(Paths.get("/Users/aomsir/MyStudyProject/Java/Netty/netty-basic-01/data.txt"), StandardOpenOption.READ);

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
