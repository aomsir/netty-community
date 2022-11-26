package com.aomsir.nio02;



import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Aomsir
 * @Date: 2022/10/16
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNIO9 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put("奥".getBytes());

        buffer.flip();
        CharBuffer result = StandardCharsets.UTF_8.decode(buffer);
        System.out.println("result = " + result);
    }
}
