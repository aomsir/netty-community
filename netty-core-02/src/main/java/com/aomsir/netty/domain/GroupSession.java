package com.aomsir.netty.domain;

import io.netty.channel.Channel;

import java.util.*;

/**
 * @Author: Aomsir
 * @Date: 2022/11/24
 * @Description: 群组消息会话实体类
 * @Email: info@say521.cn
 * @GitHub: <a href="https://github.com/aomsir">GitHub</a>
 */
public class GroupSession {

    // 存储聊天室的信息(静态的,不归对象所管)
    private static final Map<String, Group> groupMap = new HashMap<>();

    public Group createGroup(String name, Set<String> members) {
        Group group = new Group(name, members);

        // 不存在再进行创建
        return groupMap.putIfAbsent(name, group);
    }

    // 获取聊天室的成员
    public Set<String> getMembers(String name) {
        return groupMap.get(name).getMembers();
    }


    // 获取群组中群员的Channel
    public List<Channel> getMembersChannel(String name) {
        List<Channel> retList = new ArrayList<>();

        Set<String> members = this.getMembers(name);
        for (String member : members) {
            Session session = new Session();

            // 因为session中的两个map都是static的，所以可以直接获取channel
            Channel channel = session.getChannel(member);
            retList.add(channel);
        }

        return retList;
    }
}
