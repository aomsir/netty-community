package com.aomsir.netty09;

import com.sun.deploy.net.HttpRequest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Aomsir
 * @Date: 2022/11/8
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class NettyHttpServer1 {

    private static final Logger log2 = LoggerFactory.getLogger(NettyHttpServer1.class);
    private static final Logger log1 = LoggerFactory.getLogger(NettyHttpServer1.class);
    private static final Logger log = LoggerFactory.getLogger(NettyHttpServer1.class);

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.group(new NioEventLoopGroup());


        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast(new LoggingHandler());
                pipeline.addLast(new HttpServerCodec());  // 与http有关的编解码器,接受请求时解码,接受响应时编码
                pipeline.addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                        log.error("{}", msg);

                        if (msg instanceof io.netty.handler.codec.http.HttpRequest) {
                            io.netty.handler.codec.http.HttpRequest request = (io.netty.handler.codec.http.HttpRequest) msg;

                            HttpHeaders headers = request.headers(); // 请求头

                            String uri = request.uri(); // 请求行
                            HttpVersion httpVersion = request.protocolVersion();
                            HttpMethod method = request.method();
                            log1.error("{},{},{}", uri, headers,method);

                            DefaultFullHttpResponse response = new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.OK); // 构建响应状态行
                            byte[] bytes = "<b>Hello,Aomsir</b>".getBytes();
                            response.headers().set("CONTENT_LENGTH", bytes.length);
                            response.content().writeBytes(bytes);

                            ctx.writeAndFlush(response);
                        } else if (msg instanceof HttpContent) {
                            // 请求体
                            HttpContent content = (HttpContent) msg;
                            ByteBuf content1 = ((HttpContent) msg).content();
                        }
                        super.channelRead(ctx, msg);
                    }
                });
            }
        });

        serverBootstrap.bind(8000);
    }
}
