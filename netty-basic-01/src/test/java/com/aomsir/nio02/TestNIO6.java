package com.aomsir.nio02;

import java.nio.ByteBuffer;

/**
 * @Author: Aomsir
 * @Date: 2022/10/16
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNIO6 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a','b','c','d'});

        buffer.flip();
        System.out.println("buffer.get() = " + (char) buffer.get());  // a
        System.out.println("buffer.get() = " + (char) buffer.get());  // b

        buffer.mark();  // 打标记
        System.out.println("buffer.get() = " + (char) buffer.get());  // c
        System.out.println("buffer.get() = " + (char) buffer.get());  // d

        buffer.reset();  // 跳回标记点
        System.out.println("buffer.get() = " + (char) buffer.get());  // c
        System.out.println("buffer.get() = " + (char) buffer.get());  // d

    }
}
