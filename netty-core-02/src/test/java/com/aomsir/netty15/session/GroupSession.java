package com.aomsir.netty15.session;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GroupSession {
    private static final Map<String, Group> groupMap = new ConcurrentHashMap<>();

    public Group createGroup(String name, Set<String> members) {
        Group group = new Group(name, members);
        return groupMap.putIfAbsent(name, group);
    }

    public Set<String> getMembers(String name) {
        return groupMap.get(name).getMembers();
    }

    public List<Channel> getMembersChannel(String name) {
        List<Channel> retChannels = new ArrayList();
        Set<String> members = getMembers(name);
        for (String member : members) {
            Channel channel = new Session().getChannel(member);
            retChannels.add(channel);
        }
        return retChannels;
    }
}
