package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

// 用于接收客户端的数据
class Receiver extends Thread {
    private DataInputStream in;
    ClientHandler client;

    boolean START = true;
    public Receiver(Socket clientSocket,ClientHandler client) {
        try {
            in = new DataInputStream(clientSocket.getInputStream());
            this.client=client;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (START) {
            try {
                String receivedData = in.readUTF();
            //    System.out.println(receivedData);

                if (!receivedData.isEmpty()) {
                 //   System.out.println("Received data: " + receivedData);
                    Inspection.CM.add(new CliAndMsg(this.client, receivedData));
                }
            } catch (IOException e) {
                if (e instanceof SocketException && e.getMessage().equals("Socket closed")) {
                 //   System.out.println("Socket is closed.");
                    START = false;
                    break;
                } else {
                    e.printStackTrace();
                }
            }
        }



    }


    void stopRun(){

        this.START=false;
    }



}