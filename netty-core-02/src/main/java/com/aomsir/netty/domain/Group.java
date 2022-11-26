package com.aomsir.netty.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

/**
 * @Author: Aomsir
 * @Date: 2022/11/24
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@Data
@ToString
public class Group {

    private String groupName;

    // 用户的用户名
    private Set<String> members;

    public Group(String groupName, Set<String> members) {
        this.groupName = groupName;
        this.members = members;
    }
}
