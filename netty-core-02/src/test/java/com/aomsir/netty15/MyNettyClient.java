package com.aomsir.netty15;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.aomsir.netty15.message.GroupChatRequestMessage;
import com.aomsir.netty15.message.GroupCreateRequestMessage;
import com.aomsir.netty15.message.ChatRequestMessage;
import com.aomsir.netty15.message.LoginRequestMessage;
import com.aomsir.netty15.message.LoginResponseMessage;
import com.aomsir.netty15.message.PPingMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

// channel 什么关闭？
// handler一些回调方法进行处理
// IdleStateHandler处理 关闭channel
// 异常发生 也会关闭channel
// 程序正常退出 。。

// 写内容 都要在Handler中完成。


public class MyNettyClient {
    private static final Logger log = LoggerFactory.getLogger(MyNettyClient.class);

    public static void main(String[] args) throws InterruptedException, JsonProcessingException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        MyMessageToByteEncoder myMessageToByteEncoder = new MyMessageToByteEncoder();
        MyByteToMessageDecoder myByteToMessageDecoder = new MyByteToMessageDecoder();

        Scanner scanner = new Scanner(System.in);

        CountDownLatch WAIT_LOGIN = new CountDownLatch(1);
        AtomicBoolean LOGIN = new AtomicBoolean(false);
        AtomicBoolean SEVERS_LOSE = new AtomicBoolean(false);

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            Bootstrap group = bootstrap.group(eventLoopGroup);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 7, 4, 0, 0));
                    ch.pipeline().addLast("logging", LOGGING_HANDLER);
                    ch.pipeline().addLast(myByteToMessageDecoder);
                    ch.pipeline().addLast(myMessageToByteEncoder);
                    ch.pipeline().addLast(new IdleStateHandler(0, 5, 0));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            // 触发了写空闲事件
                            if (event.state() == IdleState.WRITER_IDLE) {
                                log.debug("3s 没有写数据了，发送一个心跳包");
                                ctx.writeAndFlush(new PPingMessage("client"));
                            }
                        }
                    });
                    ch.pipeline().addLast(new IdleStateHandler(8, 0, 0));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
                            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                                log.debug("已经8s没有接受到server的信息 怀疑服务器端宕机 需要重新连接");
                                SEVERS_LOSE.set(true);
                                ctx.channel().close();
                            }
                        }
                    });

                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.debug("client read data {} ", msg);
                            if (msg instanceof LoginResponseMessage) {
                                LoginResponseMessage loginResponseMessage = (LoginResponseMessage) msg;
                                if (loginResponseMessage.isSuccess()) {
                                    LOGIN.set(true);
                                }
                                WAIT_LOGIN.countDown();
                            }
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            if (SEVERS_LOSE.get()) {
                                log.debug("断线重连...");
                                EventLoop eventLoop = ctx.channel().eventLoop();
                                eventLoop.submit(new Runnable() {
                                    @Override
                                    public void run() {
                                        //bootstrap.group(new NioEventLoopGroup());
                                        ChannelFuture connect = bootstrap.connect(new InetSocketAddress(8000));
                                        connect.addListener(new ChannelFutureListener() {
                                            @Override
                                            public void operationComplete(ChannelFuture future) throws Exception {
                                                log.debug("{}", future);
                                            }
                                        });
                                    }
                                });

                            } else {
                                log.debug("正常断开连接已经断开");
                            }
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            log.debug("连接已经断开", cause);
                            super.channelInactive(ctx);
                        }

                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {

                            new Thread(() -> {
                                System.out.println("请输入用户名: ");
                                String username = scanner.nextLine();

                                System.out.println("请输入用户名: ");
                                String password = scanner.nextLine();

                                //发送登录消息
                                LoginRequestMessage loginRequestMessage = new LoginRequestMessage(username, password);
                                ctx.writeAndFlush(loginRequestMessage);

                                //等待响应回来
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
                                    System.out.println("==================================");
                                    System.out.println("send [username] [content]");
                                    System.out.println("gsend [group name] [content]");
                                    System.out.println("gcreate [group name] [m1,m2,m3...]");
                                    System.out.println("quit");
                                    System.out.println("==================================");

                                    String command = scanner.nextLine();

                                    String[] args = command.split(" ");
                                    switch (args[0]) {
                                        case "send":
                                            ctx.writeAndFlush(new ChatRequestMessage(args[2], args[1], username));
                                            break;
                                        case "gcreate":
                                            String groupName = args[1];
                                            String[] membersItem = args[2].split(",");
                                            Set<String> members = new HashSet<>(Arrays.asList(membersItem));
                                            members.add(username);
                                            ctx.writeAndFlush(new GroupCreateRequestMessage(groupName, members));
                                            break;
                                        case "gsend":
                                            String toGroupName = args[1];
                                            String content = args[2];
                                            ctx.writeAndFlush(new GroupChatRequestMessage(username, toGroupName, content));
                                            break;
                                        case "quit":
                                            ctx.channel().close();
                                            return;
                                    }
                                }
                            }, "CLIENT UI").start();

                        }
                    });
                }
            });
            Channel channel = bootstrap.connect(new InetSocketAddress(8000)).sync().channel();
            //监控channel的关闭
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("client error ", e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }


}