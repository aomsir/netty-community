package com.aomsir.netty;

import com.aomsir.netty.message.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.aomsir.netty.codec.ChatByteToMessageDecoder;
import com.aomsir.netty.codec.ChatMessageToByteEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class MyNettyClient {

    public static void main(String[] args) throws InterruptedException, JsonProcessingException {

        Scanner scanner = new Scanner(System.in);

        CountDownLatch WAIT_LOGIN = new CountDownLatch(1);

        // 原子性的
        AtomicBoolean LOGIN = new AtomicBoolean(false);
        AtomicBoolean SERVER_ERROR = new AtomicBoolean(false);

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50);
            bootstrap.group(eventLoopGroup);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    // 封帧解码器
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 7, 4, 0, 0));
                    ch.pipeline().addLast(LOGGING_HANDLER);

                    // 编解码器(将ByteBuf转换为Message/将Message转换为ByteBuf)
                    ch.pipeline().addLast(new ChatMessageToByteEncoder());
                    ch.pipeline().addLast(new ChatByteToMessageDecoder());

                    // 心跳检测
                    ch.pipeline().addLast(new IdleStateHandler(8, 3, 0, TimeUnit.SECONDS));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
                            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                                ctx.writeAndFlush(new PingMessage("client"));
                            } else if (idleStateEvent.state() == IdleState.READER_IDLE) {
                                log.error("server 已经8秒没有响应数据了");

                                log.error("关闭channel");
                                ChannelFuture close = ctx.channel().close();

                                log.error("重连");
                                SERVER_ERROR.set(true);
                                // close.addListener(new ChannelFutureListener() {
                                //     @Override
                                //     public void operationComplete(ChannelFuture channelFuture) throws Exception {
                                //         // 进行重连
                                //     }
                                // });

                            }
                        }
                    });

                    // 从Channel中读取数据
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                        // 在接收到新的数据时被调用
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            // 直接拿msg进行读取
                            log.debug("receive data {}", msg);

                            if (msg instanceof LoginResponseMessage) {
                                LoginResponseMessage loginResponseMessage = (LoginResponseMessage) msg;
                                if (loginResponseMessage.getCode().equals("200")) {
                                    LOGIN.set(true);
                                }
                                WAIT_LOGIN.countDown();
                            }
                        }

                        // channel不活跃
                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            if (SERVER_ERROR.get()) {
                                /**
                                 * 最好不要这样子写,当前执行这行代码的线程会被netty回收,所以在这里直连不好
                                 * Channel channel = bootstrap.connect(new InetSocketAddress(8000)).sync().channel();
                                 * channel.closeFuture().sync();
                                 */

                                // 这个地方要把整个client一起放进来
                                // 从eventLoop中重新获取一个
                                EventLoop eventLoop = ctx.channel().eventLoop();
                                eventLoop.submit(()->{
                                    log.debug("重连");
                                    Channel channel = null;
                                    try {
                                        ChannelFuture connect = bootstrap.connect(new InetSocketAddress(8000));

                                        connect.addListener(promise->{
                                            log.error("{}", promise.cause());
                                        });

                                        channel = connect.sync().channel();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }

                                    try {
                                        channel.closeFuture().sync();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                });


                            } else {
                                log.error("client is closed");
                            }
                        }

                        // channel中出现异常
                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            log.error("client is closed");
                        }

                        // 在与远程节点建立连接并且连接处于活动状态时被调用
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            // 用户名、让scanner进行输入
                            // 创建线程并启动
                            new Thread(() -> {
                                System.out.println("请输入用户名:");
                                String username = scanner.nextLine();

                                System.out.println("请输入密码:");
                                String password = scanner.nextLine();

                                // 发送登录操作
                                LoginRequestMessage loginRequestMessage = new LoginRequestMessage(username, password);
                                ctx.writeAndFlush(loginRequestMessage);

                                // 等待完成
                                try {
                                    WAIT_LOGIN.await();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                                if (!LOGIN.get()) {
                                    ctx.channel().close();
                                    return;
                                }

                                while (true) {
                                    System.out.println("==========================");
                                    System.out.println("send [username] [content]");
                                    System.out.println("gCreate [group name] [m1,m2,m3...]");
                                    System.out.println("gSend [group name] [content]");
                                    System.out.println("quit");
                                    System.out.println("==========================");

                                    String command = scanner.nextLine();
                                    String[] args = command.split(" ");
                                    switch (args[0]) {
                                        case "send":
                                            ctx.writeAndFlush(new ChatRequestMessage(username, args[1], args[2]));
                                            break;
                                        case "gCreate":
                                            String groupName = args[1];
                                            String[] membersString = args[2].split(",");
                                            Set<String> members = new HashSet<>(Arrays.asList(membersString));
                                            members.add(username);
                                            ctx.writeAndFlush(new GroupCreateRequestMessage(groupName, members));
                                            break;
                                        case "gSend":
                                            String gName = args[1];
                                            String content = args[2];
                                            ctx.writeAndFlush(new GroupChatRequestMessage(username, gName, content));
                                            break;
                                        case "quit":
                                            // 服务器端解决unbind
                                            ctx.channel().close();
                                            return;
                                    }
                                }

                            }, "Client UI").start();
                        }
                    });
                }
            });
            Channel channel = bootstrap.connect(new InetSocketAddress(8000)).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("client error ", e);
        } finally {

            // 优雅的关闭连接
            eventLoopGroup.shutdownGracefully();
        }

    }
}