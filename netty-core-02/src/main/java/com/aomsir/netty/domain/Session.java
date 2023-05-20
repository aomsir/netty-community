package com.aomsir.netty.domain;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Aomsir
 * @Date: 2022/11/24
 * @Description: 会话
 * @Email: info@say521.cn
 * @GitHub: <a href="https://github.com/aomsir">GitHub</a>
 */
public class Session {

    // 用户名与channel双向绑定
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
