package com.aomsir.netty.message;

/**
 * Message类型
 */
public interface MessageType {
    // 登录请求与响应Message类型
    int LOGIN_REQUEST_MESSAGE = 1;
    int LOGIN_RESPONSE_MESSAGE = 2;


    // 个人聊天请求与响应Message类型
    int CHAT_REQUEST_MESSAGE = 3;
    int CHAT_RESPONSE_MESSAGE = 4;


    // 群组创建请求与响应Message类型
    int GROUP_CREATE_REQUEST_MESSAGE = 5;
    int GROUP_CREATE_RESPONSE_MESSAGE = 6;


    // 群组聊天请求与响应Message类型
    int GROUP_CHAT_REQUEST_MESSAGE = 7;
    int GROUP_CHAT_RESPONSE_MESSAGE = 8;


    // ping pong请求与响应Message类型
    int PING_MESSAGE = 9;
    int PONG_MESSAGE = 10;
}
