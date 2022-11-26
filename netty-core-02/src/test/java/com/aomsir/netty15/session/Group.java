package com.aomsir.netty15.session;

import lombok.Data;

import java.util.Set;

@Data
public class Group {
    // 聊天室名称
    private String name;
    // 聊天室成员
    private Set<String> members;

    public Group(String name, Set<String> members) {
        this.name = name;
        this.members = members;
    }
}
