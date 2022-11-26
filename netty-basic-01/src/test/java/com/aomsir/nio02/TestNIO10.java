package com.aomsir.nio02;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Aomsir
 * @Date: 2022/10/16
 * @Description: 粘包与compact的使用
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestNIO10 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(50);

        buffer.put("Hi Aomsir\n I love y".getBytes());
        doLineSplit(buffer);

        buffer.put("ou\nDo you like me?\n".getBytes());
        doLineSplit(buffer);
    }

    //
    private static void doLineSplit(ByteBuffer buffer) {
        buffer.flip(); // 读模式
        for (int i = 0; i < buffer.limit(); i++) {
            if (buffer.get(i) == '\n') {
                int length = i + 1 - buffer.position();  // 以免出现一行里面有多个\n
                ByteBuffer target = ByteBuffer.allocate(length);

                for (int j = 0; j < length; j++) {
                    target.put(buffer.get());
                }

                // 截取工作完成
                target.flip();
                System.out.println("StandardCharsets.UTF_8.decode(target) = " + StandardCharsets.UTF_8.decode(target));

                target.clear();
            }
        }

        buffer.compact(); // 写模式
    }
}
