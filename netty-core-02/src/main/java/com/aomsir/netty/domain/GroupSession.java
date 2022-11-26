package com.aomsir.netty.domain;

import io.netty.channel.Channel;

import java.util.*;

/**
 * @Author: Aomsir
 * @Date: 2022/11/24
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class GroupSession {

    // 存储聊天室的信息
    private static final Map<String, Group> groupMap = new HashMap<>();

    public Group createGroup(String name, Set<String> members) {
        Group group = new Group(name, members);

        // 类似put
        return groupMap.putIfAbsent(name, group);
    }

    // 获取聊天室的成员
    public Set<String> getMembers(String name) {
        return groupMap.get(name).getMembers();
    }

    // 参数为聊天室的成员
    public List<Channel> getMembersChannel(String name) {
        List<Channel> retList = new ArrayList<>();

        Set<String> members = this.getMembers(name);
        for (String member : members) {
            Session session = new Session();

            Channel channel = session.getChannel(member);
            retList.add(channel);
        }

        return retList;
    }
}
