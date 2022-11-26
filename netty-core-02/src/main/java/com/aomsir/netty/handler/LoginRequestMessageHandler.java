package com.aomsir.netty.handler;

import com.aomsir.netty.domain.Session;
import com.aomsir.netty.message.LoginRequestMessage;
import com.aomsir.netty.message.LoginResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Aomsir
 * @Date: 2022/11/24
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
@Slf4j
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {

    // mock数据
    private static Map<String, String> userDB = new HashMap<>();
    static {
        userDB.put("Aomsir1", "123456");
        userDB.put("Aomsir2", "123456");
        userDB.put("Aomsir3", "123456");
        userDB.put("Aomsir4", "123456");
        userDB.put("Aomsir5", "123456");
    }

    // 服务器专门用于登录验证的
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        log.error("login method invoke~");
        String username = msg.getUsername();
        String password = msg.getPassword();

        // DB查询,进行用户名/密码的验证
        boolean isLogin = this.login(username, password);
        if (isLogin) {
            log.error("login is ok~");
            Session session = new Session();
            session.bind(ctx.channel(), username);

            ctx.writeAndFlush(new LoginResponseMessage("200", "login success"));
        } else {
            log.error("login is error~");
            ctx.writeAndFlush(new LoginResponseMessage("500", "login failed"));
        }
    }

    private boolean login(String username, String password) {
        String storePassword = userDB.get(username);

        if (storePassword == null || !password.equals(storePassword)) {
            return false;
        }

        return true;
    }
}
