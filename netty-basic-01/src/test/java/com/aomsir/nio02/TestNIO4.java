package com.aomsir.nio02;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @Author: Aomsir
 * @Date: 2022/10/16
 * @Description: 写模式、position、limit详解
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNIO4 {
    @Test
    public void testState1() {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        System.out.println("buffer.capacity() = " + buffer.capacity());
        System.out.println("buffer.position() = " + buffer.position());
        System.out.println("buffer.limit() = " + buffer.limit());
    }


    @Test
    public void testState2() {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        buffer.put(new byte[]{'a','b','c','d'});

        System.out.println("buffer.capacity() = " + buffer.capacity());
        System.out.println("buffer.position() = " + buffer.position());
        System.out.println("buffer.limit() = " + buffer.limit());
    }

    @Test
    public void testState3() {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        buffer.put(new byte[]{'a','b','c','d'});

        buffer.flip();  // 切换读模式

        System.out.println("buffer.capacity() = " + buffer.capacity());
        System.out.println("buffer.position() = " + buffer.position());
        System.out.println("buffer.limit() = " + buffer.limit());
    }


    @Test
    public void testState4() {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        buffer.put(new byte[]{'a','b','c','d'});

        buffer.clear();  // 切换读模式

        System.out.println("buffer.capacity() = " + buffer.capacity());
        System.out.println("buffer.position() = " + buffer.position());
        System.out.println("buffer.limit() = " + buffer.limit());
    }

    @Test
    public void testState5() {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        buffer.put(new byte[]{'a','b','c','d'});

        buffer.flip();  // 切换写模式
        System.out.println("buffer.get() = " + (char) buffer.get());  // a
        System.out.println("buffer.get() = " + (char) buffer.get());  // b

        System.out.println("buffer.capacity() = " + buffer.capacity());  // 10
        System.out.println("buffer.position() = " + buffer.position());  // 2
        System.out.println("buffer.limit() = " + buffer.limit());        // 4


        System.out.println("----------------------------------");
        buffer.compact();  // 切换写模式
        System.out.println("buffer.capacity() = " + buffer.capacity());  // 10
        System.out.println("buffer.position() = " + buffer.position());  // 2
        System.out.println("buffer.limit() = " + buffer.limit());        // 10


        buffer.flip();
        System.out.println("buffer.get() = " + (char) buffer.get());     // c
    }
}
