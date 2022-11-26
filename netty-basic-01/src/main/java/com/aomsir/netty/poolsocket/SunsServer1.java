package com.aomsir.netty.poolsocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SunsServer1 {

    private static ExecutorService executorService;

    static{
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),20,
                         120L, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(1000));
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8080);

            Socket socket = null;
            while (true) {
                socket = serverSocket.accept();
                //new Thread(new SunsServerHandler(socket)).start();
                //线程池
                executorService.execute(new SunsServerHandler(socket));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}


class SunsServerHandler implements Runnable {
    private Socket socket;

    public SunsServerHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    System.out.println("line = " + line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
