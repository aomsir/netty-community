package com.aomsir.netty05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

/**
 * @Author: Aomsir
 * @Date: 2022/10/30
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestByteBuf {
    public static void main(String[] args) {
        // 获得ByteBuf,空参默认为256字节
        // 如果指定大小小于默认值,满了以后会自动扩容,256满了以后不扩容
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        buffer.writeByte('a');   // 占一个字节
        buffer.writeInt(10);     // 占四个字节

        System.out.println(buffer);
        System.out.println(ByteBufUtil.prettyHexDump(buffer));

    }
}
