package com.aomsir.netty12;

/**
 * @Author: Aomsir
 * @Date: 2022/11/13
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class TestAccount {
    public static void main(String[] args) {
        Account account = new Account();
        account.setId(1);
        account.setName("Aomsir");
        System.out.println("account = " + account);
        System.out.println("account.hashCode() = " + account.hashCode());
    }
}
