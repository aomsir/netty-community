package com.aomsir.netty.handler;

import com.aomsir.netty.domain.Group;
import com.aomsir.netty.domain.GroupSession;
import com.aomsir.netty.message.GroupCreateRequestMessage;
import com.aomsir.netty.message.GroupCreateResponseMessage;
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
public class GroupCreateMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        GroupSession groupSession = new GroupSession();

        Group group = groupSession.createGroup(msg.getGroupName(), msg.getMembers());

        log.error("{}", group);
        if (group == null) {
            // 如果成功,告知所有人群组创建成功
            List<Channel> membersChannel = groupSession.getMembersChannel(msg.getGroupName());
            for (Channel channel : membersChannel) {
                log.error("{}", channel);
                channel.writeAndFlush(new GroupCreateResponseMessage("200", "you have enter" + msg.getGroupName()));
            }
        } else {
            // 告知我这个组已经创建
            ctx.writeAndFlush(new GroupCreateResponseMessage("500", "this group has created"));
        }
    }
}
