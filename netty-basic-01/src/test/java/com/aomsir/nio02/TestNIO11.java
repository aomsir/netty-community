package com.aomsir.nio02;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * @Author: Aomsir
 * @Date: 2022/10/16
 * @Description: 将数据写入文件
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNIO11 {
    public static void main(String[] args) throws Exception{
        // 1.获取channel
        FileChannel channel = new FileOutputStream("data1.txt").getChannel();

        // 2.获取buffer
        String data = "Aomsir";
        ByteBuffer buffer = Charset.forName("UTF-8").encode(data);

        // 3.write
        channel.write(buffer);
    }
}
