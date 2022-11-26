package com.aomsir.nio02;

import java.nio.ByteBuffer;

/**
 * @Author: Aomsir
 * @Date: 2022/10/16
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNIO7 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a','b','c','d'});

        buffer.flip();
        System.out.println("buffer.get() = " + (char) buffer.get()); // a
        System.out.println("buffer.get(3) = " + (char) buffer.get(3)); // d
        System.out.println("buffer.position() = " + buffer.position());  // 1
    }
}
