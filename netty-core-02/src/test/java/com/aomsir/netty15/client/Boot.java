package com.aomsir.netty15.client;

public class Boot {
    public static void main(String[] args) {
        Client client = new Client();
        client.connect("127.0.0.1", 8000);
    }
}
