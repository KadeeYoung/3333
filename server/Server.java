package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {



    Server(){

    }
    public static void main(String[] args) {

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(11451);
            Anteroom anteroom = new Anteroom(serverSocket);
            anteroom.start();
            Inspection inspection = new Inspection();
            inspection.start();
            Control control =new Control();
            control.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }
}

