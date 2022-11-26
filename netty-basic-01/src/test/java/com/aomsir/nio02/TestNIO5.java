package com.aomsir.nio02;

import java.nio.ByteBuffer;

/**
 * @Author: Aomsir
 * @Date: 2022/10/16
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNIO5 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a','b','c','d'});

        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println("buffer.get() = " + (char) buffer.get());
        }


        System.out.println("-----------------------------");

        buffer.rewind();   // 重新获取数据(因为读完以后数据没有删除,只是position和limit重合)
        while (buffer.hasRemaining()) {
            System.out.println("buffer.get() = " + (char) buffer.get());
        }
    }
}
