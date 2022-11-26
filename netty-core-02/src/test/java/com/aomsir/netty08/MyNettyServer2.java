package com.aomsir.netty08;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @Author: Aomsir
 * @Date: 2022/10/26
 * @Description: ObjectDecoder/ObjectEncoder测试服务端
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class MyNettyServer2 {

    private static final Logger log1 = LoggerFactory.getLogger(MyNettyServer2.class);
    private static final Logger log = LoggerFactory.getLogger(MyNettyServer2.class);

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.group(new NioEventLoopGroup());

        // 接受socket缓冲区大小等同于设置了滑动窗口大小,初始值65535
        serverBootstrap.option(ChannelOption.SO_RCVBUF, 100);

        // serverBootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16, 16, 16));  // 自定义ByteBuf
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            // 获取到一个read/write操作的channel
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast(new JsonObjectDecoder());
                pipeline.addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                        ByteBuf buffer = (ByteBuf) msg;
                        String userJSON = buffer.toString(Charset.defaultCharset());
                        log.error("{}", userJSON);

                        ObjectMapper objectMapper = new ObjectMapper();
                        User user = objectMapper.readValue(userJSON, User.class);
                        log.error("user object is {}", user);
                        super.channelRead(ctx, msg);
                    }
                });
                pipeline.addLast(new LoggingHandler());

            }
        });

        serverBootstrap.bind(8000);
    }
}
