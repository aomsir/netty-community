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
public class TestByteBuf2 {
    public static void main(String[] args) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);

        byteBuf.writeByte(5);

        System.out.println("byteBuf = " + byteBuf);
        System.out.println(ByteBufUtil.prettyHexDump(byteBuf));

        byte b = byteBuf.readByte();
        System.out.println("b = " + b);
        System.out.println("byteBuf = " + byteBuf);
        System.out.println(ByteBufUtil.prettyHexDump(byteBuf));
    }
}
