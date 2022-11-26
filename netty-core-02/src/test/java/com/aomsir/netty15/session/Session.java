package com.aomsir.netty15.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {

    private static final Map<String, Channel> usernameChannelMap = new ConcurrentHashMap<>();
    private static final Map<Channel, String> channelUsernameMap = new ConcurrentHashMap<>();

    public void bind(Channel channel, String username) {
        usernameChannelMap.put(username, channel);
        channelUsernameMap.put(channel, username);
    }

    public void unbind(Channel channel) {
        String username = channelUsernameMap.remove(channel);
        if(username!=null){
            usernameChannelMap.remove(username);
        }
    }

    public Channel getChannel(String username) {
        return usernameChannelMap.get(username);
    }


    public String toString() {
        return usernameChannelMap.toString();
    }
}
