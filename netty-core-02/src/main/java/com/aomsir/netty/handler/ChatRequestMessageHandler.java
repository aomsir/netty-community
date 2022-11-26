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
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String toUserName = msg.getTo();
        String content = msg.getContent();
        Session session = new Session();

        Channel channel = session.getChannel(toUserName);
        if (channel != null) {
            channel.writeAndFlush(new ChatResponseMessage("200", "chat ok", msg.getFrom(), content));
        } else {
            ctx.writeAndFlush(new ChatResponseMessage("200", "chat failed"));
        }
    }
}
