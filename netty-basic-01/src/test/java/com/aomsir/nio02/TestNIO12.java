package com.aomsir.nio02;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @Author: Aomsir
 * @Date: 2022/10/16
 * @Description: 传统方式复制
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNIO12 {
    public static void main(String[] args) throws Exception{
        // FileInputStream inputStream = new FileInputStream("/Users/aomsir/MyStudyProject/Java/Netty/netty-basic-01/data.txt");
        // FileOutputStream outputStream = new FileOutputStream("/Users/aomsir/MyStudyProject/Java/Netty/netty-basic-01/data2.txt");
        //
        // byte[] buffer = new byte[1024];
        //
        // while (true) {
        //     int read = inputStream.read(buffer);
        //     if (-1 == read) {
        //        break;
        //    }
        //
        //    outputStream.write(buffer,0,read);
        // }

        // inputStream.close();
        // outputStream.close();


        FileChannel from = new FileInputStream("/Users/aomsir/MyStudyProject/Java/Netty/netty-basic-01/data.txt").getChannel();
        FileChannel to = new FileOutputStream("/Users/aomsir/MyStudyProject/Java/Netty/netty-basic-01/data2.txt").getChannel();

        // from.transferTo(0,from.size(),to);

        long left = from.size();
        while (left > 0) {
            left = left - from.transferTo(from.size() - left ,left,to);
        }
    }
}
