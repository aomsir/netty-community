package com.aomsir.netty11;

import java.io.Serializable;

/**
 * @Author: Aomsir
 * @Date: 2022/11/10
 * @Description: DTO、数据传输对象
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class Message implements Serializable {
    private String username;
    private String password;

    public Message() {
    }

    public Message(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
