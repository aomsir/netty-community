package com.aomsir.netty15.handler;

import com.aomsir.netty15.message.ChatRequestMessage;
import com.aomsir.netty15.message.ChatResponseMessage;
import com.aomsir.netty15.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String toUserName = msg.getTo();
        Session session = new Session();

        Channel channel = session.getChannel(toUserName);
        if(channel!=null){
           channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(),msg.getContent()));
        }else{
            ctx.writeAndFlush(new ChatResponseMessage(false,"对方用户不存在"));
        }


    }
}
