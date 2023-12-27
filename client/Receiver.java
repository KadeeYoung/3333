package client;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receiver extends Thread{

    DataInputStream in;
    boolean RSTART = true;
    boolean namejud=false;
    public Receiver(Socket client){
        try {
            in =new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run(){
        while (RSTART){
            try {
                String receive=in.readUTF();
             //   System.out.println("收到信息:" + receive);
                if(receive.contains("serversays:name重复")){
                    namejud=true;
                   // System.out.println("退出judge");
                    RSTART = false;
                    break;
                } else if (receive.contains("serversays:你被踢出去了")) {
                   // System.out.println("你被踢出去了");
                    Client.enterRoom.setEnabled(true);
                    Client.exitRoom.setEnabled(false);
                    Client.sendButton.setEnabled(false);
                    JOptionPane.showMessageDialog(Client.chatArea, "你被踢出去了！");
                    RSTART = false;
                    break;
                } else if (receive.contains("serversays:服务器终止")) {
                    Client.enterRoom.setEnabled(true);
                    Client.exitRoom.setEnabled(false);
                    Client.sendButton.setEnabled(false);
                    JOptionPane.showMessageDialog(Client.chatArea, "服务器已断开！");
                    RSTART = false;
                    break;

                } else {
                    Client.chatArea.append("\n");
                    Client.chatArea.append(receive);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(Client.chatArea, "服务器已断开！");
                RSTART = false;
            }


        }
    }
    void stopRun(){
        this.RSTART=false;
    }
    boolean NameJudge(){
        return namejud;
    }
}
