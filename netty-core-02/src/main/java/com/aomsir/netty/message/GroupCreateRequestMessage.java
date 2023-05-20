package com.aomsir.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


/**
 * 群组创建Message
 * 包含 群组内容、成员列表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupCreateRequestMessage extends Message {

    private String groupName;

    private Set<String> members;

    @Override
    public int getMessageType() {
        return MessageType.GROUP_CREATE_REQUEST_MESSAGE;
    }
}
