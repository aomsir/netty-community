package com.aomsir.netty.handler;

import com.aomsir.netty.domain.Session;
import com.aomsir.netty.message.ChatRequestMessage;
import com.aomsir.netty.message.ChatResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: Aomsir
 * @Date: 2022/11/24
 * @Description: 消息发送处理器(自定义处理器)
 * @Email: info@say521.cn
 * @GitHub: <a href="https://github.com/aomsir">GitHub</a>
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String toUserName = msg.getTo();    // 获取收件人信息
        String content = msg.getContent();  // 获取内容
        Session session = new Session();    //

        // 获取收信人的Channel
        Channel channel = session.getChannel(toUserName);
        if (channel != null) {
            // 将要发送的消息封装为ChatResponseMessage，通过Channel发送给收信人
            channel.writeAndFlush(new ChatResponseMessage("200", "chat ok", msg.getFrom(), content));
        } else {
            // 给当前自己的Channel返回消息(ctx上下文)
            ctx.writeAndFlush(new ChatResponseMessage("200", "chat failed"));
        }
    }
}
