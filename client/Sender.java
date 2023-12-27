package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Sender extends Thread {
    private DataOutputStream out;
    private String name;
    private BlockingQueue<String> messageQueue;
    private boolean SSTART=true;

    public Sender(Socket client, String name) {
        try {
            out = new DataOutputStream(client.getOutputStream());
            this.name = name;
            messageQueue = new LinkedBlockingQueue<>();
            SSTART = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (SSTART) {
            try {
                String message = messageQueue.take();
                send(message);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void send(String s) throws IOException {

        if (s != null) {
            out.writeUTF(s);
            out.flush();
        }
    }


    public void enqueueMessage(String message) {
        messageQueue.add(message);
    }


    public String getuserName() {
        return name;
    }
    void stopRun(){
        this.SSTART=false;
    }
}
