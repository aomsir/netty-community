package com.aomsir.netty06;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

/**
 * @Author: Aomsir
 * @Date: 2022/11/2
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestByteBuf3 {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);

        buffer.writeBytes(new byte[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'});

        System.out.println(buffer);
        System.out.println(ByteBufUtil.prettyHexDump(buffer));  // 输出ByteBuf内存结构

        ByteBuf s1 = buffer.slice(0, 6);
        System.out.println("s1 = " + s1);
         s1.retain();

        ByteBuf s2 = buffer.slice(6, 4);
        System.out.println("s2 = " + s2);
         s2.retain();

        buffer.release();  //
        System.out.println(ByteBufUtil.prettyHexDump(s1));
        System.out.println(ByteBufUtil.prettyHexDump(s2));
    }
}
