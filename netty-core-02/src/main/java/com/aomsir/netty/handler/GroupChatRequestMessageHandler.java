package com.aomsir.netty.handler;

import com.aomsir.netty.domain.Group;
import com.aomsir.netty.domain.GroupSession;
import com.aomsir.netty.message.GroupChatRequestMessage;
import com.aomsir.netty.message.GroupChatResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author: Aomsir
 * @Date: 2022/11/24
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
@ChannelHandler.Sharable
@Slf4j
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        String from = msg.getFrom();
        String groupName = msg.getGroupName();
        String content = msg.getContent();

        GroupSession groupSession = new GroupSession();
        List<Channel> membersChannel = groupSession.getMembersChannel(groupName);
        for (Channel channel : membersChannel) {
            log.error("{}", channel);
            channel.writeAndFlush(new GroupChatResponseMessage(from, content));
        }
    }
}
