package com.aomsir.nio02;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @Author: Aomsir
 * @Date: 2022/10/16
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNIO8 {
    public static void main(String[] args) {
        // capacity为字符串长度
        ByteBuffer buffer = Charset.forName("UTF-8").encode("aomsir");

        // 不用写读模式
        // buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println("buffer.get() = " + (char) buffer.get());
        }

        buffer.clear();

    }
}
