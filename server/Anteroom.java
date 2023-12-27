package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// 专门用于等候新的聊客连接并建立通信通道。

class Anteroom extends Thread {

    private final ServerSocket serverSocket;
    static List<ClientHandler> people = new ArrayList<>();
    public Anteroom(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true)
        {
            try {
                // 等待新的聊客连接
                Socket clientSocket = serverSocket.accept();
                // 创建新的线程来处理与该客户端的通信
                ClientHandler clientHandler = new ClientHandler(clientSocket);

                clientHandler.start();
                people.add(clientHandler);

                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }




}
