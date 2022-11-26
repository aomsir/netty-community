package com.aomsir.netty.domain;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Aomsir
 * @Date: 2022/11/24
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class Session {

    private static final Map<String, Channel> usernameChannelMap = new HashMap<>();
    private static final Map<Channel, String> channelUserMap = new HashMap<>();

    public void bind(Channel channel, String username) {
        usernameChannelMap.put(username, channel);
        channelUserMap.put(channel, username);
    }

    public void unbind(Channel channel) {
        String username = channelUserMap.remove(channel);
        if (username != null) {
            usernameChannelMap.remove(username);
        }
    }

    public Channel getChannel(String username) {
        return usernameChannelMap.get(username);
    }
}
