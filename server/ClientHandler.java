package server;

import java.io.IOException;
import java.net.Socket;

// 用于处理与单个客户端的通信逻辑
class ClientHandler extends Thread {
    public Socket clientSocket;

    public Sender sender;
    public Receiver receiver;

    String name;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;


    }

    @Override
    public void run() {
        try {
            // 创建 Receiver 线程
            receiver = new Receiver(clientSocket,this);
            receiver.start();


            sender = new Sender(clientSocket);
            //sender.send("asd-asd");

            // 等待 Receiver 和 Sender 线程完成
            receiver.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws IOException {
        this.receiver.stopRun();
        clientSocket.close();

    }


    public String getsName() {
        return name;
    }
}
