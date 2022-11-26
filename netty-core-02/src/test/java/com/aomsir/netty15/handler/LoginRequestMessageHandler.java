package com.aomsir.netty15.handler;

import com.aomsir.netty15.message.LoginRequestMessage;
import com.aomsir.netty15.message.LoginResponseMessage;
import com.aomsir.netty15.session.Session;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {

    private static Map<String, String> usersDB = new HashMap<>();

    static {
        usersDB.put("suns1", "123456");
        usersDB.put("suns2", "123456");
        usersDB.put("suns3", "123456");
        usersDB.put("suns4", "123456");
        usersDB.put("suns5", "123456");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        log.debug("登录操作....");
        String username = msg.getUsername();
        String password = msg.getPassword();

        boolean isLoginOK = login(username, password);

        if (isLoginOK) {
            log.debug("登录验证成功.. ");
            Session session = new Session();
            session.bind(ctx.channel(),username);
            ctx.writeAndFlush(new LoginResponseMessage(true, "login is ok"));
        } else {
            log.debug("登录验证失败....");
            ctx.writeAndFlush(new LoginResponseMessage(false, "login is error"));
        }
    }

    private boolean login(String username, String password) {
        String dbPassword = usersDB.get(username);
        if(dbPassword == null || !dbPassword.equals(password)){
            return false;
        }
        return true;
    }
}
