package com.aomsir.netty15.clienthandler;

import com.aomsir.netty.message.GroupChatRequestMessage;
import com.aomsir.netty.message.GroupCreateRequestMessage;
import com.aomsir.netty15.client.Client;
import com.aomsir.netty15.message.ChatRequestMessage;
import com.aomsir.netty15.message.LoginRequestMessage;
import com.aomsir.netty15.message.LoginResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class UIHandler extends ChannelInboundHandlerAdapter {
    private CountDownLatch WAIT_LOGIN = new CountDownLatch(1);
    private AtomicBoolean LOGIN = new AtomicBoolean(false);
    private Scanner scanner = new Scanner(System.in);

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
        log.debug("连接已经断开");
        EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.submit(() -> {
            Client client = new Client();
            client.connect("127.0.0.1", 8000);
        });
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
}
